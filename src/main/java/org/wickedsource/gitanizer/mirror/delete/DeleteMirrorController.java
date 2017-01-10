package org.wickedsource.gitanizer.mirror.delete;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.wickedsource.gitanizer.core.ResourceNotFoundException;
import org.wickedsource.gitanizer.core.WorkdirConfiguration;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;
import org.wickedsource.gitanizer.mirror.sync.SubgitImportService;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@Transactional
public class DeleteMirrorController {

    public static final String COUNTER_ACTIVE_TASKS = "gitanizer.counter.deleteTasks.currentlyActive";

    public static final String COUNTER_QUEUED_TASKS = "gitanizer.counter.deleteTasks.currentlyQueued";

    public static final String GAUGE_MAX_PARALLEL_TASKS = "gitanizer.gauge.deleteTasks.maxParallel";

    public static final int MAX_THREADS_DEFAULT = 5;

    private MirrorRepository mirrorRepository;

    private WorkdirConfiguration workdirConfiguration;

    private SubgitImportService subgitImportService;

    private CounterService counterService;

    private ExecutorService executor;

    private Environment environment;

    private Logger logger = LoggerFactory.getLogger(DeleteMirrorController.class);

    @Autowired
    public DeleteMirrorController(MirrorRepository mirrorRepository, WorkdirConfiguration workdirConfiguration, SubgitImportService subgitImportService, CounterService counterService, GaugeService gaugeService, Environment environment) {
        this.mirrorRepository = mirrorRepository;
        this.workdirConfiguration = workdirConfiguration;
        this.subgitImportService = subgitImportService;
        this.counterService = counterService;
        this.environment = environment;
        int maxParallelTasks = MAX_THREADS_DEFAULT;
        String maxParallelTasksString = this.environment.getProperty("gitanizer.importTasks.maxThreads");
        if (maxParallelTasksString != null) {
            try {
                maxParallelTasks = Integer.valueOf(maxParallelTasksString);
            } catch (NumberFormatException e) {
                logger.error("Environment variable 'gitanizer.importTasks.maxThreads' has an invalid value: '%s'! Must be an integer! Defaulting to %d", maxParallelTasksString, MAX_THREADS_DEFAULT);
            }
        }
        this.executor = Executors.newFixedThreadPool(maxParallelTasks);
        initMetrics(counterService, gaugeService, maxParallelTasks);
    }

    private void initMetrics(CounterService counterService, GaugeService gaugeService, int maxParallelTasks) {
        gaugeService.submit(GAUGE_MAX_PARALLEL_TASKS, maxParallelTasks);
        // incrementing and decrementing counters once to initialize them to 0
        counterService.increment(COUNTER_ACTIVE_TASKS);
        counterService.decrement(COUNTER_ACTIVE_TASKS);
        counterService.increment(COUNTER_QUEUED_TASKS);
        counterService.decrement(COUNTER_QUEUED_TASKS);
    }

    @GetMapping("/mirrors/{id}/delete")
    public String deleteMirror(@PathVariable long id) {
        Mirror mirror = mirrorRepository.findOne(id);
        if (mirror == null) {
            throw new ResourceNotFoundException();
        }
        Path workdir = workdirConfiguration.getWorkdir(mirror.getId());
        mirrorRepository.delete(id);
        if (subgitImportService.isImportRunning(mirror.getId())) {
            subgitImportService.cancelImport(mirror.getId());
        }
        deleteWorkdirAsync(workdir);
        return "redirect:/mirrors/list";
    }

    /**
     * Deletes the given path recursively and starts a new thread for doing so, since deletion of
     * git repository clones may be time consuming because of a lot of small files. Exposes the following
     * metrics to Spring Boots CounterService and GaugeService:
     * <ul>
     * <li><strong>gitanizer.counter.deleteTasks.currentlyActive:</strong> number of currently running delete tasks</li>
     * <li><strong>gitanizer.counter.deleteTasks.currentlyQueued:</strong> number of currently queued delete tasks that are waiting for a free slot</li>
     * <li><strong>gitanizer.gauge.deleteTasks.maxParallel:</strong> number of delete tasks that are allowed to run in parallel</li>
     * </ul>
     *
     * @param workdir the path to delete.
     */
    private void deleteWorkdirAsync(Path workdir) {
        Runnable task = () -> {
            try {
                counterService.decrement(COUNTER_QUEUED_TASKS);
                counterService.increment(COUNTER_ACTIVE_TASKS);
                try {
                    deleteRecursively(workdir);
                } catch (DirectoryNotEmptyException e) {
                    // import process may still be running...trying again two times before finally failing
                    try {
                        Thread.sleep(2000);
                        deleteRecursively(workdir);
                    } catch (DirectoryNotEmptyException e2) {
                        Thread.sleep(2000);
                        deleteRecursively(workdir);
                    }
                }
            } catch (Exception e) {
                String message = String.format("Exception during async deletion of workdir: %s", workdir);
                logger.error(message, e);
                throw new IllegalStateException(message, e);
            } finally {
                counterService.decrement(COUNTER_ACTIVE_TASKS);
            }
        };

        executor.submit(task);
        counterService.increment(COUNTER_QUEUED_TASKS);
    }

    private void deleteRecursively(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                file.toFile().setWritable(true);
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                dir.toFile().setWritable(true);
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });

    }

}
