package org.yoes.likechatbackend.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yoes.likechatbackend.domain.model.entities.User;
import org.yoes.likechatbackend.domain.services.UserService;
import org.yoes.likechatbackend.domain.services.UserServiceNames;
import org.yoes.likechatbackend.domain.util.security.token.UserWithTokenDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceNames userService;

    @Autowired
    private UserService userServices;


    @Value("${jwt.secret }")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        Optional<User> optionalUser = userService.findById(userId);
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("all_user_name")
    public ResponseEntity<List<UserWithTokenDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserWithTokenDTO> usersWithToken = new ArrayList<>();

        for (User user : users) {
            String token = new UserWithTokenDTO().generateToken(secretKey, expirationTime);
            UserWithTokenDTO userWithToken = new UserWithTokenDTO(user, token);
            usersWithToken.add(userWithToken);
        }

        return ResponseEntity.ok(usersWithToken);
    }



    @PostMapping("insert-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (userServices.isEmailAlreadyRegistered(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        User createdUser = userService.save(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User user) {
        if (userServices.isEmailAlreadyRegistered(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        user.setId(userId);
        User updatedUser = userService.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{userId}/email")
    public ResponseEntity<?> updateEmail(@PathVariable Long userId, @RequestParam String email) {
        if (userServices.isEmailAlreadyRegistered(email)) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        userServices.updateEmail(userId, email);
        return ResponseEntity.ok("Email updated successfully");
    }

    @PutMapping("/{userId}/name")
    public ResponseEntity<?> updateName(@PathVariable Long userId,
                                        @RequestParam(required = false) String firstName,
                                        @RequestParam(required = false) String lastName) {
        try {
            userService.updateName(userId, firstName, lastName);
            return ResponseEntity.ok("Name updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
