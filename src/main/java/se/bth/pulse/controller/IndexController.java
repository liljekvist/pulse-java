package se.bth.pulse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class is a view controller that serves the index page.
 */
@Controller
public class IndexController {

  /**
   * This method is used to render the index page.
   *
   * @param model          - used to pass attributes to the view
   * @param authentication - used to get the username and role of the logged-in user
   * @return String         - the view to be rendered
   */
  @GetMapping("/")
  public String showIndex(Model model, Authentication authentication) {
    model.addAttribute("username", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "start.jsp");
    return "public/index";
  }
}
