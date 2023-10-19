package se.bth.pulse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This class is a view controller that serves the login page.
 */
@Controller
public class LoginController {

  @GetMapping("/login")
  public String showLogin(Model model) {
    model.addAttribute("content", "signin.jsp");
    return "public/index";
  }
}
