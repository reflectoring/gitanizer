package org.wickedsource.gitanizer.mirror.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StatusMessageService {

    private final MirrorRepository mirrorRepository;

    @Autowired
    public StatusMessageService(MirrorRepository mirrorRepository) {
        this.mirrorRepository = mirrorRepository;
    }

    /**
     * Save a status message that describes that a certain progress is made in the
     * synchronization process.
     *
     * @param mirrorId   ID of the mirror for which to store the status message.
     * @param percentage percentage of the progress.
     */
    public void progress(Long mirrorId, int percentage) {
        saveMessage(mirrorId, String.format("Synchronizing. Progress: %d%%.", percentage));
    }

    /**
     * Save a status message that describes that some kind of error was encountered in the
     * synchronization process
     *
     * @param mirrorId     ID of the mirror for which to store the status message.
     * @param errorMessage the error message to include in the status message.
     */
    public void error(Long mirrorId, String errorMessage) {
        saveMessage(mirrorId, String.format("Error during Synchronization: %s.", errorMessage));
    }

    /**
     * Save a status message that describes that the synchronization process is currently finished
     * and the local mirror is up-to-date to the remote repository.
     *
     * @param mirrorId ID of the mirror for which to store the status message.
     */
    public void upToDate(Long mirrorId) {
        saveMessage(mirrorId, "Mirror is up-to-date.");
    }

    /**
     * Save a status message that describes that the synchronization process is currently paused.
     *
     * @param mirrorId ID of the mirror for which to store the status message.
     */
    public void paused(Long mirrorId) {
        saveMessage(mirrorId, "Synchronization paused.");
    }

    /**
     * Save a status message that describes that the synchronization process just started and is currently
     * initializing.
     *
     * @param mirrorId ID of the mirror for which to store the status message.
     */
    public void syncStarted(Long mirrorId) {
        saveMessage(mirrorId, "Synchronization initializing.");
    }

    private void saveMessage(Long mirrorId, String message) {
        Mirror mirror = mirrorRepository.findOne(mirrorId);
        if (mirror != null) {
            // mirror may be null when deleted
            mirror.setLastUpdated(LocalDateTime.now());
            mirror.setLastStatusMessage(message);
            mirrorRepository.save(mirror);
        }
    }

}
