package org.wickedsource.gitanizer.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String displayLoginPage() {
        return "login";
    }

    @GetMapping(value = "/login-error")
    public String displayLoginPage(Model model) {
        return "login";
    }

}
