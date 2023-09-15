package se.bth.pulse.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileUserAdminController {

    @GetMapping(value = "/admin/users/file")
    public String file(Model model, Authentication authentication) {
        model.addAttribute("user", authentication.getName());
        model.addAttribute("role", authentication.getAuthorities().toString());
        model.addAttribute("content", "file.jsp");
        return "public/index";
    }
}
