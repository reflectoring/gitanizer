package org.wickedsource.gitanizer.mirror.importing;

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
public class ImportMirrorController {

    private MirrorRepository mirrorRepository;

    @Autowired
    public ImportMirrorController(MirrorRepository mirrorRepository) {
        this.mirrorRepository = mirrorRepository;
    }

    /**
     * Starts or resumes the synchronization of a remote SVN repository into a local git repository.
     *
     * @param id ID of the mirror entity for which to start synchronization.
     * @return redirect to the mirror list view.
     */
    @GetMapping("/mirrors/{id}/sync/start")
    public String activateAutoSync(@PathVariable long id) {
        Mirror mirror = mirrorRepository.findOne(id);
        if (mirror == null) {
            throw new ResourceNotFoundException();
        }

        mirror.setSyncActive(true);
        return "redirect:/mirrors/list";
    }

    /**
     * Stops the SVN to GIT synchronization for the Mirror with the specified ID.
     * Note that an import task that is currently running will continue!
     *
     * @param id ID of the Mirror whose synchronization to stop.
     * @return redirect to the mirror list view.
     */
    @GetMapping("/mirrors/{id}/sync/stop")
    public String deactivateAutoSync(@PathVariable long id) {
        Mirror mirror = mirrorRepository.findOne(id);
        if (mirror == null) {
            throw new ResourceNotFoundException();
        }

        mirror.setSyncActive(false);
        mirrorRepository.save(mirror);
        return "redirect:/mirrors/list";
    }

}
