package org.wickedsource.gitanizer.mirror.controller.delete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

@Controller
@Transactional
public class DeleteMirrorController {

    private MirrorRepository mirrorRepository;

    @Autowired
    public DeleteMirrorController(MirrorRepository mirrorRepository) {
        this.mirrorRepository = mirrorRepository;
    }

    @GetMapping("/mirrors/{id}/delete")
    public String deleteMirror(@PathVariable long id) {
        mirrorRepository.delete(id);
        return "/mirrors/list";
    }

}
