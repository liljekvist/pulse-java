package se.bth.pulse.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReportsController {
    @GetMapping("/reports")
    public String showReports(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("role", authentication.getAuthorities().toString());
        model.addAttribute("content", "reports.jsp");
        return "public/index";
    }
}
