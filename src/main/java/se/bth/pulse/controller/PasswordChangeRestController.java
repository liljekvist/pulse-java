package se.bth.pulse.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.repository.UserRepository;
import se.bth.pulse.service.UserDetailsImpl;

/**
 * This class is a rest controller that serves the password change page.
 */
@RestController
@OpenAPIDefinition(info = @Info(title = "PasswordChangeRestController", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class PasswordChangeRestController {

  PasswordChangeRestController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  private final UserRepository userRepository;

  /**
   * This method is used to change the password of the logged-in user.
   * It gets the username of the logged-in user from the authentication object.
   * Then it gets the user object from the database using the username.
   * Using the user object, it sets the new password and saves the user object.
   * (Saving means updating the user object in the database when an id is already set)
   *
   * @param password          - the new password
   * @param authentication    - used to get the username of the logged-in user
   * @return ResponseEntity   - the response entity containing a success message if the password
   *                            was changed successfully, an error message otherwise.
   */
  @PostMapping(value = "/change-password")
  public ResponseEntity passwordChange(String password, Authentication authentication) {
    try {
      UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
      var userObj = userRepository.findByEmail(user.getUsername());
      userObj.setPassword(new BCryptPasswordEncoder().encode(password));
      userObj.setCredentialsExpired(false);
      userRepository.save(userObj);

      return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Password change failed", HttpStatus.BAD_REQUEST);
    }
  }

}
