package org.wickedsource.gitanizer.mirror.update;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.wickedsource.gitanizer.core.DateProvider;
import org.wickedsource.gitanizer.core.ResourceNotFoundException;
import org.wickedsource.gitanizer.mirror.domain.Mirror;
import org.wickedsource.gitanizer.mirror.domain.MirrorRepository;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;

@Controller
@Transactional
public class UpdateMirrorController {

    private MirrorRepository mirrorRepository;

    private DateProvider dateProvider;

    @Autowired
    public UpdateMirrorController(MirrorRepository mirrorRepository, DateProvider dateProvider) {
        this.mirrorRepository = mirrorRepository;
        this.dateProvider = dateProvider;
    }

    @GetMapping(value = "/mirrors/{id}/update")
    public String displayForm(Model model, @PathVariable Long id) {
        Mirror mirror = mirrorRepository.findOne(id);
        if (mirror == null) {
            throw new ResourceNotFoundException();
        }

        UpdateMirrorForm form = new UpdateMirrorForm();
        form.setId(id);
        form.setRepositoryName(mirror.getDisplayName());
        form.setRemoteSvnUrl(mirror.getRemoteSvnUrl().toString());
        form.setSvnPassword(mirror.getSvnPassword());
        form.setSvnUsername(mirror.getSvnUsername());

        model.addAttribute("form", form);
        return "/mirrors/update";
    }

    @PostMapping(value = "/mirrors/{id}/update")
    public String updateMirror(@ModelAttribute("form") @Valid UpdateMirrorForm form, BindingResult bindingResult) {
        try {
            Mirror mirror = mirrorRepository.findOne(form.getId());
            if (mirror == null) {
                throw new ResourceNotFoundException();
            }

            if (mirrorRepository.countByDisplayNameExcludeId(form.getRepositoryName(), form.getId()) > 0) {
                bindingResult.rejectValue("repositoryName", "mirrorForm.repositoryName.duplicate");
            }

            if (bindingResult.hasErrors()) {
                return "/mirrors/update";
            }

            mirror.setLastStatusUpdate(dateProvider.now());
            mirror.setDisplayName(form.getRepositoryName());
            mirror.setRemoteSvnUrl(new URL(form.getRemoteSvnUrl()));
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
            // attribute "workdirName" is not updated, so that the workdir remains intact

            mirrorRepository.save(mirror);

            return "redirect:/mirrors/list";
        } catch (MalformedURLException e) {
            throw new IllegalStateException("invalid URL!", e);
        }
    }

}
