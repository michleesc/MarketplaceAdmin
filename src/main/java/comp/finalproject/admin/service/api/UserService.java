package comp.finalproject.admin.service.api;

import comp.finalproject.admin.entity.Role;
import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.model.UserRequest;
import comp.finalproject.admin.repository.RoleRepository;
import comp.finalproject.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Component("apiUserService")
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }
    public ResponseEntity<?> addUser(UserRequest userRequest) {
        // Logika untuk menambahkan user
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role role = roleRepository.findByName(userRequest.getRoles());
        user.setRoles(Arrays.asList(role));
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getUserById(long userId) {
        // Logika untuk membaca user berdasarkan ID
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> updateUser(long userId, UserRequest userRequest) {
        // Logika untuk mengupdate user
        User existingUser = userRepository.findById(userId).orElse(null);
        System.out.println(existingUser.getId());
        if (existingUser != null) {
            existingUser.setName(userRequest.getName());
            existingUser.setEmail(userRequest.getEmail());
            existingUser.setPassword(userRequest.getPassword());
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            Role role = roleRepository.findByName(userRequest.getRoles());
            existingUser.setRoles(new ArrayList<>(Arrays.asList(role)));
            userRepository.save(existingUser);
            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> deleteUser(long userId) {
        // Logika untuk menghapus user dengan soft delete
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            // Set atribut deleted menjadi true atau lakukan langkah sesuai kebutuhan
            user.setDeleted(true);
            userRepository.save(user);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
