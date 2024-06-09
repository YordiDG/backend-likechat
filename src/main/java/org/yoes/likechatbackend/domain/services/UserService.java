package org.yoes.likechatbackend.domain.services;

import org.yoes.likechatbackend.domain.model.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);


    void incrementFailedLoginAttempts(User user);

    void updatePassword(User user, String newPassword);

    void resetPassword(String resetToken, String email, String newPassword);

    boolean authenticate(String email, String password);

    User save(User user);

    void updateEmail(Long userId, String email);

    boolean isEmailAlreadyRegistered(String email);

    void resetFailedLoginAttempts(User user);

    void uploadPhoto(User user, byte[] photoData);

    User register(User user);


}

