package se.bth.pulse.controller;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Reader;
import java.io.StringReader;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Role;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.RoleRepository;
import se.bth.pulse.repository.UserRepository;

/**
 * This class is a rest controller that serves the file page.
 * The page is only accessible if the user is logged in as an admin.
 * It is used to upload a csv file containing users.
 */
@RestController
public class FileRestController {

  Logger logger = LoggerFactory.getLogger(FileRestController.class);
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final JavaMailSender emailSender;

  FileRestController(
      UserRepository userRepository,
      RoleRepository roleRepository,
      JavaMailSender emailSender
  ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.emailSender = emailSender;
  }

  /**
   * This method is used to check if the csv file is valid then convert it to a list of users.
   * The list of users is then returned as a response entity. The response entity is represented
   * as a json object.
   *
   * @param file                      - the csv file containing the users
   * @param request                   - used to get the csrf token
   * @return ResponseEntity           - the response entity containing the users if the file is
   *                                    valid, an error message otherwise.
   *                                    It is represented as a json object.
   */
  @PostMapping(value = "/api/admin/file/check")
  public ResponseEntity<Object> check(@RequestBody String file, HttpServletRequest request) {
    logger.atInfo().log("File checked: " + file);
    CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
    logger.info("{}={}", token.getHeaderName(), token.getToken());
    try (Reader reader = new StringReader(file.trim())) {

      // create csv bean reader
      var csv = new CsvToBeanBuilder<User>(reader)
          .withType(User.class)
          .withIgnoreLeadingWhiteSpace(true)
          .build();

      Role defaultRole = roleRepository.findByName("default");

      List<User> users = csv.parse();
      for (User user : users) {
        user.setPassword(new BCryptPasswordEncoder().encode(
            RandomStringUtils.random(20, 0, 0, true, true, null,
                new SecureRandom()))); // Kommer sättas igen vid submit
        user.setEnabled(false);
        user.setRole(defaultRole);
      }
      List<CsvException> exceptions = csv.getCapturedExceptions();

      if (!exceptions.isEmpty()) {
        HashMap<String, String> error = new HashMap<>();
        logger.error("Number of Mistakes: {}", exceptions.size());
        for (CsvException exception : exceptions) {
          logger.error("Error at line {}: {}", exception.getLineNumber(), exception.getMessage());
          error.put("error",
              "Error at line " + exception.getLineNumber() + ": " + exception.getMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
    } catch (Exception ex) {
      HashMap<String, String> error = new HashMap<>();
      error.put("error", ex.getMessage());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Takes a list of users and saves them to the database.
   * The users are created with a random password and the role "default".
   * The users are also sent an email with their password.
   * The method is transactional, meaning that if an error occurs during the saving of the users,
   * the database will roll back to the state before the method was called.
   *
   * @param userList                  - the list of users to be saved to the database
   * @return ResponseEntity           - Contains the status code of the request
   */
  @PostMapping(value = "/api/admin/file/upload", consumes = "application/json")
  @Transactional(rollbackFor = {Exception.class})
  public ResponseEntity<Object> upload(@RequestBody List<User> userList) {
    try {
      Role defaultRole = roleRepository.findByName("default");
      for (User user : userList) {
        String password = RandomStringUtils.random(20, 0, 0, true, true, null, new SecureRandom());
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setEnabled(false);
        user.setRole(defaultRole);
        userRepository.save(user);
        sendUserCreationEmail(user.getEmail(), password);
      }

      // Now, userList contains your CSV data as a list of User objects
      for (User user : userList) {
        logger.info(user.getEmail() + ", " + user.getFirstname() + ", " + user.getLastname() + ", "
            + user.getPhonenr());
      }

      return new ResponseEntity<>(HttpStatus.OK);

    } catch (Exception ex) {
      HashMap<String, String> error = new HashMap<>();
      error.put("error", ex.getMessage());
      logger.error(ex.getMessage());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
  }

  private void sendUserCreationEmail(String email, String password) {
    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(email);
      message.setFrom("projectpulse1337@gmail.com");
      message.setSubject("Pulse - Account Created");
      message.setText(String.format(
          "Your account has been created. Your password is: %s\nPlease login and change it.",
          password));
      emailSender.send(message);
    } catch (Exception e) {
      logger.error("Error sending email: " + e.getMessage());
      throw e;
    }

  }
}
