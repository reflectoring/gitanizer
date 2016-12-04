package org.wickedsource.gitanizer.mirror.controller.sync;

import org.wickedsource.gitanizer.status.domain.StatusMessageService;
import org.wickedsource.gitanizer.subgit.ImportCommandListener;

/**
 * Listener that listens on events during a subgit import and saves a StatusMessage for each
 * event.
 */
public class StatusMessageListener implements ImportCommandListener {

    private final StatusMessageService statusMessageService;

    private final Long mirrorId;

    public StatusMessageListener(Long mirrorId, StatusMessageService statusMessageService) {
        this.mirrorId = mirrorId;
        this.statusMessageService = statusMessageService;
    }

    @Override
    public void onProgress(int percentage) {
        statusMessageService.progress(mirrorId, percentage);
    }

    @Override
    public void onError(String errorMessage) {
        statusMessageService.error(mirrorId, errorMessage);
    }

}
