package org.wickedsource.gitanizer.mirror.create;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.wickedsource.gitanizer.core.DateProvider;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Controller
@Transactional
public class CreateMirrorController {

    private MirrorRepository mirrorRepository;

    private DateProvider dateProvider;

    @Autowired
    public CreateMirrorController(MirrorRepository mirrorRepository, DateProvider dateProvider) {
        this.mirrorRepository = mirrorRepository;
        this.dateProvider = dateProvider;
    }

    @GetMapping(value = "/mirrors/create")
    public String displayForm(Model model) {
        model.addAttribute("form", new CreateMirrorForm());
        return "/mirrors/create";
    }

    @PostMapping(value = "/mirrors/create")
    public String createMirror(@ModelAttribute("form") @Valid CreateMirrorForm form, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return "/mirrors/create";
            }
            Mirror mirror = new Mirror();
            mirror.setDisplayName(form.getRepositoryName());
            mirror.setRemoteSvnUrl(new URL(form.getRemoteSvnUrl()));
            mirror.setLastStatusUpdate(dateProvider.now());
            mirror.setWorkdirName(UUID.randomUUID().toString());
            mirrorRepository.save(mirror);
            return "redirect:/mirrors/list";
        } catch (MalformedURLException e) {
            throw new IllegalStateException("invalid URL!", e);
        }
    }

}
