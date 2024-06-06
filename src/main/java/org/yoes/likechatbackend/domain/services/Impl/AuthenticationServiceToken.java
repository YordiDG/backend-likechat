package org.yoes.likechatbackend.domain.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yoes.likechatbackend.domain.model.entities.User;
import org.yoes.likechatbackend.domain.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationServiceToken {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void generateResetToken(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }

    public void resetPassword(String email, String resetToken, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getResetToken().equals(resetToken)) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetToken(null); // Clear the reset token
                userRepository.save(user);
            } else {
                throw new IllegalArgumentException("Invalid reset token");
            }
        } else {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
    }
}
