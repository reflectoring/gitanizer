package org.wickedsource.gitanizer.mirror.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.wickedsource.gitanizer.core.ResourceNotFoundException;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

@Controller
@Transactional
public class SynchronizeMirrorController {

    private MirrorRepository mirrorRepository;

    private SubgitImportService subgitImportService;

    @Autowired
    public SynchronizeMirrorController(MirrorRepository mirrorRepository, SubgitImportService subgitImportService) {
        this.mirrorRepository = mirrorRepository;
        this.subgitImportService = subgitImportService;
    }

    /**
     * Starts or resumes the synchronization of a remote SVN repository into a local git repository.
     *
     * @param id ID of the mirror entity for which to start synchronization.
     * @return redirect to the mirror list view.
     */
    @GetMapping("/mirrors/{id}/sync/start")
    public String startSynchronization(@PathVariable long id) {
        Mirror mirror = mirrorRepository.findOne(id);
        if (mirror == null) {
            throw new ResourceNotFoundException();
        }

        if (!subgitImportService.isImportRunning(mirror.getId())) {
            subgitImportService.startImport(mirror);
        }

        mirror.setSyncActive(true);
        return "redirect:/mirrors/list";
    }

    /**
     * Stops the SVN to GIT synchronization for the Mirror with the specified ID.
     *
     * @param id ID of the Mirror whose synchronization to stop.
     * @return redirect to the mirror list view.
     */
    @GetMapping("/mirrors/{id}/sync/stop")
    public String stopSynchronization(@PathVariable long id) {
        Mirror mirror = mirrorRepository.findOne(id);
        if (mirror == null) {
            throw new ResourceNotFoundException();
        }

        if (subgitImportService.isImportRunning(mirror.getId())) {
            subgitImportService.cancelImport(mirror.getId());
        }

        mirror.setSyncActive(false);
        return "redirect:/mirrors/list";
    }

}
