package org.wickedsource.gitanizer.mirror.controller.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@Transactional
public class ListMirrorsController {

    private MirrorRepository mirrorRepository;

    @Autowired
    public ListMirrorsController(MirrorRepository mirrorRepository) {
        this.mirrorRepository = mirrorRepository;
    }

    @GetMapping({"/", "/mirrors/list"})
    public String listMirrors(Model model) {
        Iterable<Mirror> mirrors = mirrorRepository.findAll();

        List<MirrorDTO> mirrorDTOs = new ArrayList<>();
        for (Mirror mirror : mirrors) {
            MirrorDTO dto = new MirrorDTO();
            dto.setId(mirror.getId());
            dto.setName(mirror.getName());
            dto.setLastChangeDate(mirror.getLastUpdated());
            dto.setSyncStatus(mirror.isSyncStatus());
            // TODO: add latest status message
            dto.setLastStatusMessage("Test");
            mirrorDTOs.add(dto);
        }

        model.addAttribute("mirrors", mirrorDTOs);
        return "/mirrors/list";
    }

}
