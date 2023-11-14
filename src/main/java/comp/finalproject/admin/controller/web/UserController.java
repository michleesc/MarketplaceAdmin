package comp.finalproject.admin.controller.web;


import comp.finalproject.admin.dto.UserDto;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.repository.UserRepository;
import comp.finalproject.admin.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model, Principal principal) {
        String email = principal.getName();

        User user = userRepository.findByEmail(email);
        String name = user.getName();

        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);

        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "user/users";
    }
}

