package se.bth.pulse.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import se.bth.pulse.Entity.Role;
import se.bth.pulse.Entity.User;
import se.bth.pulse.Repository.RoleRepository;
import se.bth.pulse.Repository.UserRepository;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

@RestController
public class FileRestController {

    FileRestController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Autowired
    private JavaMailSender emailSender;

    Logger logger = LoggerFactory.getLogger(FileRestController.class);
    UserRepository userRepository;
    RoleRepository roleRepository;

    @PostMapping(value = "/api/admin/file/check")
    public ResponseEntity<Object> check(@RequestBody String file, HttpServletRequest request) {
        logger.atInfo().log("File checked: " + file);
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        logger.info("{}={}", token.getHeaderName(), token.getToken());
        try (Reader reader = new StringReader(file.trim())) {

            // create csv bean reader
            CsvToBean<User> csv = new CsvToBeanBuilder(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Role default_role = roleRepository.findByName("default");

            List<User> users = csv.parse();
            for (User user : users) {
                user.setPassword(new BCryptPasswordEncoder().encode(RandomStringUtils.randomAlphanumeric(12))); // Kommer sättas igen vid submit
                user.setEnabled(false);
                user.setRole(default_role);
            }
            List<CsvException> exceptions = csv.getCapturedExceptions();

            if (!exceptions.isEmpty()) {
                HashMap<String, String> error = new HashMap<>();
                logger.error("Number of Mistakes: {}", exceptions.size());
                for (CsvException exception : exceptions) {
                    logger.error("Error at line {}: {}", exception.getLineNumber(), exception.getMessage());
                    error.put("error", "Error at line " + exception.getLineNumber() + ": " + exception.getMessage());
                }
                return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Object>(users, HttpStatus.OK);
        } catch (Exception ex) {
            HashMap<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/admin/file/upload", consumes = "application/json")
    @Transactional(rollbackFor = { Exception.class })
    public ResponseEntity<Object> upload(@RequestBody List<User> userList) {
        try {
            Role default_role = roleRepository.findByName("default");
            for (User user : userList) {
                String password = RandomStringUtils.randomAlphanumeric(12);
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                user.setEnabled(false);
                user.setRole(default_role);
                userRepository.save(user);
                sendUserCreationEmail(user.getEmail(), password);
            }

            // Now, userList contains your CSV data as a list of User objects
            for (User user : userList) {
                logger.info(user.getEmail() + ", " + user.getFirstname() + ", " + user.getLastname() + ", " + user.getPhonenr());
            }

            return new ResponseEntity<Object>(HttpStatus.OK);

        } catch (Exception ex) {
            HashMap<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
        }
    }

    private void sendUserCreationEmail(String email, String password){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setFrom("projectpulse1337@gmail.com");
            message.setSubject("Pulse - Account Created");
            message.setText(String.format("Your account has been created. Your password is: %s\nPlease login and change it.", password));
            emailSender.send(message);
        } catch (Exception e) {
            logger.error("Error sending email: " + e.getMessage());
            throw e;
        }

    }
}
