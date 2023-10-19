package se.bth.pulse.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.bth.pulse.entity.Setting;
import se.bth.pulse.repository.SettingRepository;

/**
 * This class is a view controller that serves the setup page.
 */
@Controller
public class SetupController {

  private final SettingRepository settingRepository;

  public SetupController(SettingRepository settingRepository) {
    this.settingRepository = settingRepository;
  }

  /**
   * Serves the setup page to the user if the setup is not done, otherwise redirects to the home
   * page.
   *
   * @param model - used to pass attributes to the view
   * @return String   - the view to be rendered
   */
  @GetMapping("/setup")
  public String showSetup(Model model) {
    List<Setting> result = settingRepository.findByName("setup_done");
    if (result.isEmpty()) {
      model.addAttribute("content", "setup.jsp");
      return "public/index";
    } else {
      return "redirect:/";
    }
  }
}
