package org.yoes.likechatbackend.domain.services.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yoes.likechatbackend.domain.model.entities.User;
import org.yoes.likechatbackend.domain.repository.UserRepository;
import org.yoes.likechatbackend.domain.services.UserService;
import org.yoes.likechatbackend.domain.util.security.token.JwtUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtUtil tokenService;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password format.");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public void updateEmail(Long userId, String email) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (isEmailAlreadyRegistered(email)) {
                throw new IllegalArgumentException("Email already registered");
            }
            user.setEmail(email);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
    }


    @Override
    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void incrementFailedLoginAttempts(User user) {
        int maxFailedAttempts = 3; // Máximo de intentos fallidos permitidos
        int maxTempBanAttempts = 5; // Máximo de intentos fallidos antes del bloqueo temporal

        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
        user.setLastFailedLogin(LocalDateTime.now());

        if (user.getFailedLoginAttempts() >= maxFailedAttempts) {
            if (user.getFailedLoginAttempts() >= (maxFailedAttempts + maxTempBanAttempts)) {
                // Bloquear la cuenta por 10 días si se excede el límite de intentos fallidos
                user.setAccountBlockedUntil(LocalDateTime.now().plusDays(10));
            } else {
                // Bloquear la cuenta temporalmente por una hora si se excede el límite inicial
                user.setAccountBlockedUntil(LocalDateTime.now().plusHours(1));
            }
        }

        userRepository.save(user);
    }

    @Override
    public void resetFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(0);
        user.setLastFailedLogin(null);
        user.setAccountBlockedUntil(null);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new IllegalArgumentException("Invalid password format.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private boolean isValidPassword(String password) {

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

    @Override
    public boolean authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Verificar la contraseña hasheada
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Resetear los intentos fallidos si la autenticación es exitosa
                user.setFailedLoginAttempts(0);
                userRepository.save(user);
                return true;
            } else {
                // Incrementar los intentos fallidos y bloquear la cuenta si supera el límite
                incrementFailedLoginAttempts(user);
                return false;
            }
        }
        return false;
    }

    @Override
    public void resetPassword(String resetToken, String email, String newPassword) {
        // Verificar la validez del token y la dirección de correo electrónico
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!tokenService.isValidResetToken(user, resetToken)) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }
        updatePassword(user, newPassword);
    }

    @Override
    public void uploadPhoto(User user, byte[] photoData) {
        user.setPhoto(photoData);
        userRepository.save(user);
    }

    @Override
    public User register(User user) {
        String email = user.getEmail();

        if (!isValidEmailDomain(email)) {
            throw new IllegalArgumentException("Invalid email domain");
        }

        if (isEmailAlreadyRegistered(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password format.");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }

    private boolean isValidEmailDomain(String email) {
        String domain = email.substring(email.lastIndexOf("@") + 1);

        return domain.equalsIgnoreCase("gmail.com") ||
                domain.equalsIgnoreCase("hotmail.com") ||
                domain.equalsIgnoreCase("outlook.com");
    }

}
