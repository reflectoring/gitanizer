package org.wickedsource.gitanizer.mirror;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Transactional
public class MirrorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String listMirrors(Model model) {
        model.addAttribute("name", "tom");
        return "listMirrors";
    }

    @RequestMapping(value = "/mirrors/{id}/edit", method = RequestMethod.GET)
    public String editMirror(Model model) {
        return "editMirror";
    }

}
