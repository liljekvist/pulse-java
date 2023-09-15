package se.bth.pulse.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.bth.pulse.Repository.UserRepository;

@Controller
public class UserAdminController {

    private final UserRepository userRepository;

    UserAdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/admin/users")
    public String showUsers(Model model, Authentication authentication) {
        model.addAttribute("user", authentication.getName());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("role", authentication.getAuthorities().toString());
        model.addAttribute("content", "user-admin.jsp");
        return "public/index";
    }
}
