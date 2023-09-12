package se.bth.pulse.Controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    Logger logger = LoggerFactory.getLogger(FileRestController.class);
    UserRepository userRepository;
    RoleRepository roleRepository;

    @PostMapping(value = "/api/file/check")
    public ResponseEntity<Object> check(@RequestBody String file) {
        logger.atInfo().log("File checked: " + file);
        try (Reader reader = new StringReader(file.trim())) {

            // create csv bean reader
            CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            final List<User> users = csvToBean.parse();
            List<CsvException> exceptions = csvToBean.getCapturedExceptions();

            if (!exceptions.isEmpty()) {
                HashMap<String, String> error = new HashMap<>();
                logger.error("Number of Mistakes: {}", exceptions.size());
                for (CsvException exception : exceptions) {
                    logger.error("Error at line {}: {}", exception.getLineNumber(), exception.getMessage());
                    error.put("error", "Error at line " + exception.getLineNumber() + ": " + exception.getMessage());
                }
                return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception ex) {
            HashMap<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/file/upload")
    public ResponseEntity<Object> upload(@RequestBody String file) {

        try (Reader reader = new StringReader(file.trim())) {
            // Create a CsvToBean object and specify the mapping class
            CsvToBean<User> csvToBean = new CsvToBeanBuilder<User>(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // Parse the CSV file and create a list of User objects
            List<User> userList = csvToBean.parse();

            Role default_role = roleRepository.findByName("default");

            for (User user : userList) {
                user.setPassword(new BCryptPasswordEncoder().encode("abc123"));
                user.setEnabled(false);
                user.setRole(default_role);
                userRepository.save(user);
            }

            // Now, userList contains your CSV data as a list of User objects
            for (User user : userList) {
                System.out.println(user.getEmail() + ", " + user.getFirstname() + ", " + user.getLastname() + ", " + user.getPhonenr());
            }

            return new ResponseEntity<Object>(HttpStatus.OK);

        } catch (Exception ex) {
            HashMap<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
