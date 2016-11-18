package org.wickedsource.gitanizer.mirror;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorNameSanitizer;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import static org.wickedsource.gitanizer.core.Routes.*;

@Controller
@Transactional
public class CreateMirrorController {

    private MirrorRepository mirrorRepository;

    private MirrorNameSanitizer mirrorNameSanitizer;

    @Autowired
    public CreateMirrorController(MirrorRepository mirrorRepository, MirrorNameSanitizer mirrorNameSanitizer) {
        this.mirrorRepository = mirrorRepository;
        this.mirrorNameSanitizer = mirrorNameSanitizer;
    }

    @GetMapping(value = CREATE_MIRROR)
    public String displayForm(Model model) {
        model.addAttribute("form", new CreateMirrorForm());
        return CREATE_MIRROR;
    }

    @PostMapping(value = CREATE_MIRROR)
    public String createMirror(@ModelAttribute("form") @Valid CreateMirrorForm form, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return CREATE_MIRROR;
        }

        try {
            String sanitizedName = mirrorNameSanitizer.sanitizeName(form.getRepositoryName());
            Mirror mirror = new Mirror();
            mirror.setLastUpdated(LocalDateTime.now());
            mirror.setName(sanitizedName);
            mirror.setRemoteSvnUrl(new URL(form.getRemoteSvnUrl()));

            mirrorRepository.save(mirror);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid URL! Should have been validated earlier!", e);
        }

        return redirect(LIST_MIRRORS);
    }

}
