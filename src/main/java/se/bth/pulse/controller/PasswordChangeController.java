package se.bth.pulse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class is a view controller that serves the change password page.
 */
@Controller
public class PasswordChangeController {

  /**
   * Shows the password change page to the user.
   *
   * @param model          - used to pass attributes to the view
   * @param authentication - used to get the username of the logged-in user
   * @return String         - the name of the view to be rendered
   */
  @GetMapping("/change-password")
  public String showPasswordChange(Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "change-password.jsp");
    return "public/index";
  }

}
