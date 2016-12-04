package org.wickedsource.gitanizer.mirror.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.wickedsource.gitanizer.core.ResourceNotFoundException;
import org.wickedsource.gitanizer.core.SubgitConfiguration;
import org.wickedsource.gitanizer.core.WorkdirConfiguration;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;
import org.wickedsource.gitanizer.status.domain.StatusMessageService;
import org.wickedsource.gitanizer.subgit.ImportCommand;

import java.nio.file.Path;

@Controller
@Transactional
public class SynchronizeMirrorController {

    private MirrorRepository mirrorRepository;

    private WorkdirConfiguration workdirConfiguration;

    private SubgitConfiguration subgitConfiguration;

    private ImportCommandRunner importCommandRunner;

    private StatusMessageService statusMessageService;

    @Autowired
    public SynchronizeMirrorController(MirrorRepository mirrorRepository, WorkdirConfiguration workdirConfiguration, SubgitConfiguration subgitConfiguration, ImportCommandRunner importCommandRunner, StatusMessageService statusMessageService) {
        this.mirrorRepository = mirrorRepository;
        this.workdirConfiguration = workdirConfiguration;
        this.subgitConfiguration = subgitConfiguration;
        this.importCommandRunner = importCommandRunner;
        this.statusMessageService = statusMessageService;
    }

    /**
     * Starts or resumes the synchronization of a remote SVN repository into a local git repository.
     *
     * @param id ID of the mirror entity into which to synchronize.
     * @return redirect to the mirror list view.
     */
    @GetMapping("/mirrors/{id}/sync/start")
    public String startSynchronization(@PathVariable long id) {
        // TODO: only start if not started yet!
        Mirror mirror = mirrorRepository.findOne(id);
        if (mirror == null) {
            throw new ResourceNotFoundException();
        }

        Path subgitPath = subgitConfiguration.getSubgitExecutable();
        Path workdir = workdirConfiguration.getSubWorkdir(mirror.getName());

        StatusMessageListener listener = new StatusMessageListener(mirror.getId(), statusMessageService);

        ImportCommand importCommand = new ImportCommand(subgitPath.toString())
                .withTargetGitPath(workdir.toString())
                .withSourceSvnUrl(mirror.getRemoteSvnUrl().toString())
                .withListener(listener);

        statusMessageService.syncStarted(mirror.getId());
        importCommandRunner.runImportCommand(importCommand, mirror.getId());
        mirror.setSyncStatus(true);
        return "redirect:/mirrors/list";
    }

}
