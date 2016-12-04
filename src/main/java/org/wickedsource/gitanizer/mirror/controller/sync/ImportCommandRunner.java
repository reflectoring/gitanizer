package org.wickedsource.gitanizer.mirror.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.wickedsource.gitanizer.status.domain.StatusMessageService;
import org.wickedsource.gitanizer.subgit.ImportCommand;

import java.io.IOException;

@Component
public class ImportCommandRunner {

    private StatusMessageService statusMessageService;

    @Autowired
    public ImportCommandRunner(StatusMessageService statusMessageService) {
        this.statusMessageService = statusMessageService;
    }

    /**
     * Starts or resumes the subgit import of a remote SVN. Runs asynchronously since it may take a while.
     *
     * @param command  the import command containing all the parameters for the subgit import.
     * @param mirrorId ID of the mirror into which to import.
     */
    @Async
    public void runImportCommand(ImportCommand command, Long mirrorId) {
        try {
            command.execute();
            statusMessageService.upToDate(mirrorId);
        } catch (IOException e) {
            throw new IllegalStateException(String.format("IOException during async execution of subgit import command: %s", command), e);
        }
    }

}
