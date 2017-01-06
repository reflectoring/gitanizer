package org.wickedsource.gitanizer.mirror.list;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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

    /**
     * Shows a view listing all registered Mirrors.
     */
    @GetMapping({"/", "/mirrors/list"})
    public String listMirrors(Model model, HttpServletRequest request) {
        Iterable<Mirror> mirrors = mirrorRepository.findAll();

        List<MirrorDTO> mirrorDTOs = new ArrayList<>();
        for (Mirror mirror : mirrors) {
            MirrorDTO dto = new MirrorDTO();
            dto.setId(mirror.getId());
            dto.setName(mirror.getDisplayName());
            dto.setLastChangeDate(mirror.getLastStatusUpdate());
            dto.setSyncStatus(mirror.isSyncActive());
            dto.setGitCloneUrl(getGitCloneUrl(request, mirror.getGitRepositoryName()));
            if (mirror.getLastStatusMessage() == null) {
                dto.setLastStatusMessage("No status yet");
            } else {
                dto.setLastStatusMessage(mirror.getLastStatusMessage());
            }
            mirrorDTOs.add(dto);
        }

        model.addAttribute("mirrors", mirrorDTOs);
        return "/mirrors/list";
    }

    private String getGitCloneUrl(HttpServletRequest request, String gitRepositoryName) {
        return String.format("%s://%s:%d/git/%s", request.getScheme(), request.getServerName(), request.getServerPort(), gitRepositoryName);
    }

}
