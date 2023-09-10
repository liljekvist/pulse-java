package se.bth.pulse.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/setup")
public class SetupController {
    @GetMapping
    public String showSetup() {
        System.out.println("ddasd");
        return "setup";
    }
}
