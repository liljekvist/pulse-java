package se.bth.pulse.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Authority;
import se.bth.pulse.entity.Role;
import se.bth.pulse.entity.Setting;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.AuthorityRepository;
import se.bth.pulse.repository.RoleRepository;
import se.bth.pulse.repository.SettingRepository;
import se.bth.pulse.repository.UserRepository;

/**
 * This class is a rest controller that serves the setup page.
 * The page is only accessible if the setup is not done.
 * It is used to configure the application. Specifically, it is used to:
 * - configure the settings
 * - configure the admin account
 * - configure the admin role
 * - configure the default role
 */
@RestController
@OpenAPIDefinition(info = @Info(title = "SetupRestController", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class SetupRestController {

  private final SettingRepository settingRepository;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final AuthorityRepository authorityRepository;

  /**
   * Sets the repositories using dependency injection.
   * Spring automatically creates instances of the repositories then passes them to the constructor.
   *
   * @param settingRepository   - used to interact with the table Setting.
   * @param userRepository      - used to interact with the table User.
   * @param roleRepository      - used to interact with the table Role.
   */
  public SetupRestController(SettingRepository settingRepository, UserRepository userRepository,
      RoleRepository roleRepository, AuthorityRepository authorityRepository) {
    this.settingRepository = settingRepository;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.authorityRepository = authorityRepository;
  }

  /**
   * Simple method to check if the setup is done.
   *
   * @return Boolean      - true if the setup is done, false otherwise.
   */
  @GetMapping(value = "/api/setup/configure_settings")
  public Boolean configureSetting() {
    Setting setup = new Setting();
    setup.setName("setup_done");
    setup.setValue("true");

    settingRepository.save(setup);

    Authority defaultAuth = authorityRepository.findByName("default");

    // setup default role
    Role defaultRole = new Role();
    defaultRole.setName("default");
    defaultRole.setAuthorities(List.of(defaultAuth));
    roleRepository.save(defaultRole);

    return true;
  }

  /**
   * Configures the admin account and saves it to the database.
   * The admin account is used to log in to the application.
   *
   * @param email                       - the email of the admin account
   * @param password                    - the password of the admin account
   * @return ResponseEntity             - A response entity with the admin account if the account
   *                                      was created successfully, an error message otherwise.
   *                                      The response entity is used to return a JSON object.
   */
  @PostMapping(value = "/api/setup/configure_admin_account", produces = "application/json")
  public ResponseEntity<Object> configureAdminAccount(@RequestParam("email") String email,
      @RequestParam("password") String password) {
    try {

      List<Authority> auths = new ArrayList<>();
      Authority adminAuth = new Authority();
      adminAuth.setName("admin");

      auths.add(adminAuth);

      Authority defaultAuth = new Authority();
      defaultAuth.setName("default");
      
      auths.add(defaultAuth);

      auths = authorityRepository.saveAll(auths);

      Role adminRole = new Role();
      adminRole.setName("admin");
      adminRole.setAuthorities(auths);
      roleRepository.save(adminRole);

      User admin = new User();
      admin.setEmail(email);
      admin.setPassword(new BCryptPasswordEncoder().encode(password));
      admin.setEnabled(true);
      admin.setRole(adminRole);
      userRepository.save(admin);

      return new ResponseEntity<>(admin, HttpStatus.OK);
    } catch (SqlScriptException ex) {
      return new ResponseEntity<>(ex.getCause(), HttpStatus.BAD_REQUEST);
    }
  }

}
