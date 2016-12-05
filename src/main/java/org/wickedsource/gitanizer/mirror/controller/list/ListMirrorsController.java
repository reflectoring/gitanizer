package org.wickedsource.gitanizer.mirror.controller.list;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;
import org.wickedsource.gitanizer.status.domain.StatusMessage;
import org.wickedsource.gitanizer.status.domain.StatusMessageRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@Transactional
public class ListMirrorsController {

    private MirrorRepository mirrorRepository;

    private StatusMessageRepository statusMessageRepository;

    @Autowired
    public ListMirrorsController(MirrorRepository mirrorRepository, StatusMessageRepository statusMessageRepository) {
        this.mirrorRepository = mirrorRepository;
        this.statusMessageRepository = statusMessageRepository;
    }

    /**
     * Shows a view listing all registered Mirrors.
     */
    @GetMapping({"/", "/mirrors/list"})
    public String listMirrors(Model model) {
        Iterable<Mirror> mirrors = mirrorRepository.findAll();

        List<MirrorDTO> mirrorDTOs = new ArrayList<>();
        for (Mirror mirror : mirrors) {
            // Attention! Query within a loop. Should be included in the outer query for better performance if too slow.
            StatusMessage lastStatusMessage = statusMessageRepository.findTop1ByMirrorIdOrderByTimestampDesc(mirror.getId());
            MirrorDTO dto = new MirrorDTO();
            dto.setId(mirror.getId());
            dto.setName(mirror.getName());
            dto.setLastChangeDate(mirror.getLastUpdated());
            dto.setSyncStatus(mirror.isSyncActive());
            if (lastStatusMessage == null) {
                dto.setLastStatusMessage("No status yet");
            } else {
                dto.setLastStatusMessage(lastStatusMessage.getMessage());
            }
            mirrorDTOs.add(dto);
        }

        model.addAttribute("mirrors", mirrorDTOs);
        return "/mirrors/list";
    }

}
