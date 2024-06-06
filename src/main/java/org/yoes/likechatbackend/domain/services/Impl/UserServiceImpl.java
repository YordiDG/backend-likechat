package org.yoes.likechatbackend.domain.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yoes.likechatbackend.domain.model.entities.User;
import org.yoes.likechatbackend.domain.repository.UserRepository;
import org.yoes.likechatbackend.domain.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        // Validate password
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password format.");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Method to validate password format
    private boolean isValidPassword(String password) {
        // Password must be at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // Password must contain at least one letter, one digit, and one special character
        boolean containsLetter = false;
        boolean containsDigit = false;
        boolean containsSpecialCharacter = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                containsLetter = true;
            } else if (Character.isDigit(c)) {
                containsDigit = true;
            } else if (!Character.isWhitespace(c)) {
                containsSpecialCharacter = true;
            }
        }

        return containsLetter && containsDigit && containsSpecialCharacter;
    }
}
