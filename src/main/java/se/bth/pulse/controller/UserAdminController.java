package se.bth.pulse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.bth.pulse.repository.UserRepository;

/**
 * This class is a view controller that serves the user-admin page.
 * It is only accessible by users with the Role "admin".
 */
@Controller
public class UserAdminController {

  private final UserRepository userRepository;

  UserAdminController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * This method is used to render the user-admin page.
   * The return string is the name of the view to be rendered.
   *
   * @param model           - used to pass attributes to the view
   * @param authentication  - used to get the username and role of the logged-in user
   * @return                - the view to be rendered
   */
  @GetMapping("/admin/users")
  public String showUsers(Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("users", userRepository.findAll());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "user-admin.jsp");
    return "public/index";
  }
}