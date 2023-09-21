//package se.bth.pulse.Backend.Controller;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import se.bth.pulse.Entity.User;
//import se.bth.pulse.Repository.UserRepository;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//class UserImportTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    @WithMockUser(username = "test@admin.com", authorities = {"admin"})
//    void testValidImportFile() throws Exception {
//
//        mockMvc.perform(post("/admin/users/file")
//                        .contentType(MediaType.TEXT_PLAIN_VALUE)
//                        .content("email,firstname,lastname,phonenr\n" +
//                                "test@example.com,John,Doe,123456789\n" +
//                                "test2@example.com,Jane,Doe,987654321\n" +
//                                "test3@example.com,John,Doe,223355789\n"))
//                .andExpect(status().isOk());
//
//        User user = userRepository.findByEmail("test@example.com");
//        assertNotNull(user);
//        assertEquals("test@example.com", user.getEmail());
//        assertEquals("John", user.getFirstname());
//        assertEquals("Doe", user.getLastname());
//        assertEquals("123456789", user.getPhonenr());
//
//
//
//
//    }
//
//}
//
