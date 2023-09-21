package se.bth.pulse.Controller;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import java.sql.SQLException;
import java.util.List;

@RestController
public class SetupRestController {

    public SetupRestController(SettingRepository settingRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.settingRepository = settingRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    private final SettingRepository settingRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping(value = "/api/setup/configure_settings")
    public Boolean configureSetting() {
        Setting setup = new Setting();
        setup.setName("setup_done");
        setup.setValue("true");

        settingRepository.save(setup);

        // setup default role
        Role default_role = new Role();
        default_role.setName("default");
        default_role.setPremissions("r");
        roleRepository.save(default_role);

        return true;
    }

    @PostMapping(value = "/api/setup/configure_admin_account", produces = "application/json")
    public ResponseEntity<Object> configureAdminAccount(@RequestParam("email") String email, @RequestParam("password") String password) {
        try {
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

            return new ResponseEntity<Object>(admin, HttpStatus.OK);
        }
        catch (SqlScriptException ex){
            return new ResponseEntity<Object>(ex.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

}
