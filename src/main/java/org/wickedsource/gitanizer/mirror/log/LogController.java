package org.wickedsource.gitanizer.mirror.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.wickedsource.gitanizer.core.ResourceNotFoundException;
import org.wickedsource.gitanizer.mirror.sync.SubgitImportService;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
@Transactional
public class LogController {

    private MirrorRepository mirrorRepository;

    private SubgitImportService subgitImportService;


    @Autowired
    public LogController(MirrorRepository mirrorRepository, SubgitImportService subgitImportService) {
        this.mirrorRepository = mirrorRepository;
        this.subgitImportService = subgitImportService;
    }

    /**
     * Displays a page that shows the logfile of previous import runs.
     *
     * @param id    ID of the mirror whose logfile to show.
     * @param model MVC model
     * @return path to the log view.
     */
    @GetMapping("/mirrors/{id}/log")
    public String showLog(@PathVariable long id, Model model) {
        try {
            Mirror mirror = mirrorRepository.findOne(id);
            if (mirror == null) {
                throw new ResourceNotFoundException();
            }
            Path logfile = subgitImportService.getLogFile(mirror);
            String logString;
            if (Files.exists(logfile)) {
                byte[] logBytes = Files.readAllBytes(logfile);
                logString = new String(logBytes);
            } else {
                logString = "No logfile available.";
            }

            MirrorWithLogDTO mirrorDTO = new MirrorWithLogDTO();
            mirrorDTO.setName(mirror.getDisplayName());
            mirrorDTO.setLog(logString);

            model.addAttribute("mirror", mirrorDTO);

            return "/mirrors/log";
        } catch (IOException e) {
            throw new IllegalStateException("Error while accessing log file: " + e.getMessage(), e);
        }
    }

}
