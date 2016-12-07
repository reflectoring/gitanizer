package org.wickedsource.gitanizer.mirror.controller.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.wickedsource.gitanizer.core.SubgitConfiguration;
import org.wickedsource.gitanizer.core.WorkdirConfiguration;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.status.domain.StatusMessageService;
import org.wickedsource.gitanizer.subgit.ImportCommand;

import javax.annotation.PreDestroy;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Service that manages a thread pool for subgit import tasks. The maximum number of parallel threads can be configured using the
 * environment variable 'gitanizer.importTasks.maxThreads' which defaults to 5.
 */
@Service
public class SubgitImportService {

    public static final String COUNTER_ACTIVE_TASKS = "gitanizer.counter.importTasks.currentlyActive";

    public static final String COUNTER_QUEUED_TASKS = "gitanizer.counter.importTasks.currentlyQueued";

    public static final String GAUGE_MAX_PARALLEL_TASKS = "gitanizer.gauge.importTasks.maxParallel";

    private StatusMessageService statusMessageService;

    private ExecutorService executor;

    private Map<Long, Future> taskMap = new HashMap<>();

    private SubgitConfiguration subgitConfiguration;

    private WorkdirConfiguration workdirConfiguration;

    private CounterService counterService;

    private GaugeService gaugeService;

    private Logger logger = LoggerFactory.getLogger(SubgitImportService.class);

    private static final int MAX_THREADS_DEFAULT = 5;

    @Autowired
    public SubgitImportService(StatusMessageService statusMessageService, SubgitConfiguration subgitConfiguration, WorkdirConfiguration workdirConfiguration, CounterService counterService, GaugeService gaugeService, Environment environment) {
        this.statusMessageService = statusMessageService;
        this.subgitConfiguration = subgitConfiguration;
        this.workdirConfiguration = workdirConfiguration;
        this.counterService = counterService;
        this.gaugeService = gaugeService;
        int maxParallelTasks = MAX_THREADS_DEFAULT;
        String maxParallelTasksString = environment.getProperty("gitanizer.importTasks.maxThreads");
        if (maxParallelTasksString != null) {
            try {
                maxParallelTasks = Integer.valueOf(maxParallelTasksString);
            } catch (NumberFormatException e) {
                logger.error("Environment variable 'gitanizer.importTasks.maxThreads' has an invalid value: '%s'! Must be an integer! Defaulting to %d", maxParallelTasksString, MAX_THREADS_DEFAULT);
            }
        }
        this.executor = Executors.newFixedThreadPool(maxParallelTasks);
        this.gaugeService.submit(GAUGE_MAX_PARALLEL_TASKS, maxParallelTasks);
    }

    /**
     * Starts or resumes the subgit import of a remote SVN. Runs asynchronously since it may take a while. The started
     * import task can be cancelled by calling {@link #cancelImport(Long)} with the ID of the mirror.
     *
     * @param mirror the Mirror object for which to start the subgit import.
     */
    public void startImport(Mirror mirror) {
        Path subgitPath = subgitConfiguration.getSubgitExecutable();
        Path workdir = workdirConfiguration.getSubWorkdir(mirror.getName());

        try {
            StatusMessageListener listener = new StatusMessageListener(mirror.getId(), statusMessageService);

            OutputStream logOutputStream = new FileOutputStream(getLogFile(mirror).toFile(), true);
            ImportCommand importCommand = new ImportCommand(subgitPath.toString())
                    .withTargetGitPath(workdir.toString())
                    .withSourceSvnUrl(mirror.getRemoteSvnUrl().toString())
                    .withListener(listener)
                    .withLogOutputStream(logOutputStream);

            statusMessageService.syncStarted(mirror.getId());

            Runnable task = () -> {
                try {
                    counterService.decrement(COUNTER_QUEUED_TASKS);
                    counterService.increment(COUNTER_ACTIVE_TASKS);
                    importCommand.execute();
                    statusMessageService.upToDate(mirror.getId());
                    taskMap.remove(mirror.getId());
                    counterService.decrement(COUNTER_ACTIVE_TASKS);
                    logOutputStream.close();
                } catch (IOException e) {
                    throw new IllegalStateException(String.format("IOException during async execution of subgit import command: %s", importCommand), e);
                }
            };

            Future future = executor.submit(task);
            taskMap.put(mirror.getId(), future);
            counterService.increment(COUNTER_QUEUED_TASKS);
        } catch (IOException e) {
            throw new IllegalStateException("Error writing import log into file!");
        }
    }

    /**
     * Returns the file where the import log for the specified mirror is contained.
     *
     * @param mirror the mirror whose logfile to find.
     * @return Path object pointing to the logfile.
     */
    public Path getLogFile(Mirror mirror) {
        Path workdir = workdirConfiguration.getSubWorkdir(mirror.getName());
        String logFileName = String.format("%s/subgit-import.log", workdir);
        return Paths.get(logFileName);
    }

    /**
     * Cancels the subgit import of the mirror with the given ID. If no import is currently running, does nothing.
     * The import task can be resumed at a later time by calling {@link #startImport(Mirror)}.
     *
     * @param mirrorId ID of the mirror whose import to cancel.
     */
    public void cancelImport(Long mirrorId) {
        Future future = taskMap.get(mirrorId);
        if (future != null) {
            if (future.cancel(true)) {
                counterService.decrement(COUNTER_ACTIVE_TASKS);
                statusMessageService.paused(mirrorId);
            }
            taskMap.remove(mirrorId);
        }
    }

    /**
     * Returns true if there is an import task currently running for the specified mirror.
     *
     * @param mirrorId ID of the mirror for which to check if there is a task currently running.
     * @return true if there is an import task currently running.
     */
    public boolean isImportRunning(Long mirrorId) {
        return taskMap.containsKey(mirrorId);
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }

}
