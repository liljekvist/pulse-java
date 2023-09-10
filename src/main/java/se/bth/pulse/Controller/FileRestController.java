package se.bth.pulse.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileRestController {

    Logger logger = LoggerFactory.getLogger(FileRestController.class);

    @PostMapping(value = "/api/file/upload")
    public Boolean upload(@RequestBody String file) {
        logger.atInfo().log("File uploaded: " + file);
        return true;
    }
}
