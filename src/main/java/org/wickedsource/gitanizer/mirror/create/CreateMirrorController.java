package org.wickedsource.gitanizer.mirror.create;

import org.apache.commons.lang3.StringUtils;
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

            if (mirrorRepository.countByDisplayName(form.getDisplayName()) > 0) {
                bindingResult.rejectValue("displayName", "mirrorForm.displayName.duplicate");
            }

            if (mirrorRepository.countByGitRepositoryName(Mirror.getSanitizedGitRepositoryName(form.getGitRepositoryName())) > 0) {
                bindingResult.rejectValue("gitRepositoryName", "mirrorForm.gitRepositoryName.duplicate");
            }

            if (mirrorRepository.countByGitUsername(form.getGitUsername()) > 0) {
                bindingResult.rejectValue("gitUsername", "mirrorForm.gitUsername.duplicate");
            }

            if (bindingResult.hasErrors()) {
                return "/mirrors/create";
            }
            Mirror mirror = new Mirror();
            mirror.setDisplayName(form.getDisplayName());
            mirror.setRemoteSvnUrl(new URL(form.getRemoteSvnUrl()));
            mirror.setLastStatusUpdate(dateProvider.now());
            mirror.setWorkdirName(UUID.randomUUID().toString());
            mirror.setGitRepositoryName(form.getGitRepositoryName());

            if (!StringUtils.isEmpty(form.getSvnPassword())) {
                mirror.setSvnPassword(form.getSvnPassword());
            } else {
                mirror.setSvnPassword(null);
            }

            if (!StringUtils.isEmpty(form.getSvnUsername())) {
                mirror.setSvnUsername(form.getSvnUsername());
            } else {
                mirror.setSvnUsername(null);
            }

            if (!StringUtils.isEmpty(form.getGitPassword())) {
                mirror.setGitPassword(form.getGitPassword());
            } else {
                mirror.setGitPassword(null);
            }

            if (!StringUtils.isEmpty(form.getGitUsername())) {
                mirror.setGitUsername(form.getGitUsername());
            } else {
                mirror.setGitUsername(null);
            }

            mirrorRepository.save(mirror);
            return "redirect:/mirrors/list";
        } catch (MalformedURLException e) {
            throw new IllegalStateException("invalid URL!", e);
        }
    }

}
