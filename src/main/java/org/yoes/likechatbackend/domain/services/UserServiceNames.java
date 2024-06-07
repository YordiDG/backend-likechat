package org.yoes.likechatbackend.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yoes.likechatbackend.domain.model.entities.User;
import org.yoes.likechatbackend.domain.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceNames {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }


    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public void updateName(Long userId, String firstName, String lastName) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (firstName != null) {
                user.setFirstname(firstName);
            }
            if (lastName != null) {
                user.setLastname(lastName);
            }
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
