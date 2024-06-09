package org.yoes.likechatbackend.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yoes.likechatbackend.domain.model.entities.User;
import org.yoes.likechatbackend.domain.repository.UserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    public User register(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (!isValidEmailDomain(email)) {
            throw new IllegalArgumentException("Dominio de correo electrónico inválido");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres y contener al menos una letra mayúscula, una letra minúscula, un dígito y un carácter especial.");
        }

        user.setPassword(passwordEncoder.encode(password));
        // Se pueden agregar validaciones adicionales aquí, como la longitud de la contraseña, etc.

        return userRepository.save(user);
    }

    private boolean isValidEmailDomain(String email) {
        String domain = email.substring(email.lastIndexOf("@") + 1);

        return domain.equalsIgnoreCase("gmail.com") ||
                domain.equalsIgnoreCase("hotmail.com") ||
                domain.equalsIgnoreCase("outlook.com");
    }

    private boolean isValidPassword(String password) {
        // Al menos 8 caracteres, una mayúscula, una minúscula, un dígito y un carácter especial
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }
}
