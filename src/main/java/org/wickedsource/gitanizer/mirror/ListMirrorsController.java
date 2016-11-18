package org.wickedsource.gitanizer.mirror;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.wickedsource.gitanizer.core.Routes;

@Controller
@Transactional
public class ListMirrorsController {

    @GetMapping({Routes.ROOT, Routes.LIST_MIRRORS})
    public String listMirrors(Model model) {
        model.addAttribute("name", "tom");
        return Routes.LIST_MIRRORS;
    }

}
