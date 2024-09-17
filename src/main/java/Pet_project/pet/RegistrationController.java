package Pet_project.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Tables.User;

import org.slf4j.Logger;
import java.util.Map;
import org.slf4j.LoggerFactory;

@Controller
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserForm()); // Ensure this line is present
        return "register"; // The name of the Thymeleaf template
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username, 
                                           @RequestParam String password, 
                                           @RequestParam String role) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password)); // Encode the password
            user.setRoles(role); // Set the role
            user.setEnabled(true); // Ensure the user is enabled

            userRepository.save(user); // Save the user to the database
            
            // Log user details for debugging
            logger.info("User registered: {}", user);

            return ResponseEntity.ok().body(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            logger.error("Registration failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Try Another Username"));
        }
    }

    
}
