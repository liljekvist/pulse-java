package se.bth.pulse.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FileController {

    @GetMapping(value = "/file")
    public String file() {
        return "file";
    }
}
