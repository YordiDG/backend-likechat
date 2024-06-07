package org.yoes.likechatbackend.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yoes.likechatbackend.domain.model.entities.User;
import org.yoes.likechatbackend.domain.services.Impl.AuthenticationServiceToken;
import org.yoes.likechatbackend.domain.services.UserService;
import org.yoes.likechatbackend.domain.util.security.token.Impl.AuthenticationService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationServiceToken authenticationServiceToken;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody Map<String, String> credentials)
            throws Exception {
        String email = credentials.get("email");
        String password = credentials.get("password");

        try {
            String token = authenticationService.authenticate(email, password);
            return ResponseEntity.ok(token);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = authenticationService.register(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam("token") String resetToken,
                                           @RequestParam("email") String email,
                                           @RequestParam("newPassword") String newPassword) {
        try {
            userService.resetPassword(resetToken, email, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
