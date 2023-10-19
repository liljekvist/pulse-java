package se.bth.pulse.controller;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.UserRepository;

/**
 * This class is a rest controller that serves the user admin page.
 */
@RestController
public class UserAdminRestController {

  private final UserRepository userRepository;

  Logger logger = LoggerFactory.getLogger(UserAdminRestController.class);

  public UserAdminRestController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Used to update the users profile. The user is retrieved from the user repository using the id.
   * The user firstname, lastname, email, phonenr and credentialsExpired are updated. To make the
   * user change password the credentialsExpired is set to true.
   *
   * @param id                 - the id of the user to be edited
   * @param firstname          - the new firstname of the user
   * @param lastname           - the new lastname of the user
   * @param email              - the new email of the user
   * @param phonenr            - the new phonenr of the user
   * @param credentialsExpired - the new credentialsExpired of the user
   * @return ResponseEntity   - the response entity containing a success message if the user
   */
  @PostMapping("/api/admin/user/edit")
  public ResponseEntity editUser(Integer id, String firstname, String lastname, String email,
      String phonenr, Boolean credentialsExpired) {
    try {
      Optional<User> user = userRepository.findById(id);
      if (user.isPresent()) {
        User u = user.get();
        u.setFirstname(firstname);
        u.setLastname(lastname);
        u.setEmail(email);
        u.setPhonenr(phonenr);
        logger.info("Credentials expired: " + credentialsExpired);
        u.setCredentialsExpired(credentialsExpired);
        userRepository.save(u);
        return new ResponseEntity<>(HttpStatus.OK);
      } else {
        throw new IllegalArgumentException("User not found");
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Used to disable a user permanently.
   *
   * @param id - the id of the user to be enabled
   * @return ResponseEntity   - the response entity containing a success message if the user
   */
  @PostMapping("/api/admin/user/disable/{id}")
  public ResponseEntity disableUser(@PathVariable("id") Integer id) {
    try {
      Optional<User> user = userRepository.findById(id);
      if (user.isPresent()) {
        User u = user.get();
        u.setEnabled(false);
        userRepository.save(u);
        return new ResponseEntity<>(HttpStatus.OK);
      } else {
        throw new IllegalArgumentException("User not found");
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }


}
