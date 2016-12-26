package org.wickedsource.gitanizer.mirror.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(value = "gitanizer.scheduling.disabled", havingValue = "false", matchIfMissing = true)
public class MirrorSyncService {

    public static final int DEFAULT_INTERVAL = 5 * 60;

    private SubgitImportService subgitImportService;

    private MirrorRepository mirrorRepository;

    private Environment environment;

    private int intervalInSeconds = DEFAULT_INTERVAL;

    private Logger logger = LoggerFactory.getLogger(MirrorSyncService.class);

    @Autowired
    public MirrorSyncService(SubgitImportService subgitImportService, MirrorRepository mirrorRepository, Environment environment) {
        this.subgitImportService = subgitImportService;
        this.mirrorRepository = mirrorRepository;
        this.environment = environment;
        String importIntervalString = this.environment.getProperty("gitanizer.importTasks.intervalInSeconds");
        if (importIntervalString != null) {
            try {
                intervalInSeconds = Integer.valueOf(importIntervalString);
            } catch (NumberFormatException e) {
                logger.error("Environment variable 'gitanizer.importTasks.intervalInSeconds' has an invalid value: '%s'! Must be an integer! Defaulting to %d", importIntervalString, DEFAULT_INTERVAL);
            }
        }
    }

    /**
     * Runs regularly to start an import job for all mirrors whose import interval has passed and whose
     * sync status is active.
     */
    @Scheduled(fixedDelay = 5000)
    public void syncMirrors() {
        LocalDateTime dateThreshold = LocalDateTime.now();
        dateThreshold.minusSeconds(intervalInSeconds);
        List<Mirror> eligibleMirrors = mirrorRepository.findByLastImportFinishedOlderThan(dateThreshold);

        // only sync mirrors that don't currently have an import job running
        List<Mirror> mirrorsToSync = eligibleMirrors
                .stream()
                .filter(mirror -> !subgitImportService.isImportRunning(mirror.getId()))
                .collect(Collectors.toList());
        for (Mirror mirror : mirrorsToSync) {
            subgitImportService.startImport(mirror);
        }
    }

}
