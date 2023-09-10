package se.bth.pulse.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.bth.pulse.Entity.Setting;
import se.bth.pulse.Repository.RoleRepository;
import se.bth.pulse.Repository.SettingRepository;
import se.bth.pulse.Repository.UserRepository;

import java.util.List;

@Controller
public class SetupController {

    public SetupController(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    private final SettingRepository settingRepository;

    @GetMapping("/setup")
    public String showSetup(Model model) {
        List<Setting> result = settingRepository.findByName("setup_done");
        if (result.isEmpty())
            return "setup";
        else
            return "redirect:/";
    }
}
