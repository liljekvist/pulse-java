package se.bth.pulse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.Entity.Role;
import se.bth.pulse.Entity.Setting;
import se.bth.pulse.Entity.User;
import se.bth.pulse.Repository.RoleRepository;
import se.bth.pulse.Repository.SettingRepository;
import se.bth.pulse.Repository.UserRepository;

import java.util.List;

@RestController
public class SetupController {

    public SetupController(SettingRepository settingRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.settingRepository = settingRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private final SettingRepository settingRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @GetMapping(value = "/api/setup/is_setup_done")
    public Boolean getSetupSetting() {
        List<Setting> result = settingRepository.findByName("setup_done");
        return !(result.isEmpty());
    }

    @GetMapping(value = "/api/setup/configure_settings")
    public Boolean configureSetting() {
        Setting setup = new Setting();
        setup.setName("setup_done");
        setup.setValue("true");

        settingRepository.save(setup);

        return true;
    }

    @PostMapping(value = "/api/setup/configure_admin_account")
    public Boolean configureAdminAccount(@RequestParam("email") String email, @RequestParam("password") String password) {
        Role admin_role = new Role();
        admin_role.setName("admin");
        admin_role.setPremissions("rwx");
        roleRepository.save(admin_role);

        User admin = new User();
        admin.setEmail(email);
        admin.setPassword(new BCryptPasswordEncoder().encode(password));
        admin.setEnabled(true);
        admin.setRole(admin_role);
        userRepository.save(admin);

        return true;
    }

}
