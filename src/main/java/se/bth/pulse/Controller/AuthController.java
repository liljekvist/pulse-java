//package se.bth.pulse.Controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Controller
//public class AuthController {
//
//    @GetMapping("/login")
//    public String login() {
//        return "login"; // login.html
//    }
//
//    @GetMapping("/register")
//    public String registration() {
//        return "registration"; // registration.html
//    }
//
//    // Handle registration form submission
////    @PostMapping("/register")
////    public String register(UserRegistrationDto registrationDto) {
////        // Implement user registration logic here
////        // You can use the UserRepository to save the new user
////        // Don't forget to hash the password before saving it
////        // Redirect to the login page or another page after successful registration
////        return "redirect:/login";
////    }
//}
