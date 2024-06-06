package org.yoes.likechatbackend.domain.services;

import org.yoes.likechatbackend.domain.model.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);

    User save(User user);

    boolean isEmailAlreadyRegistered(String email);
}

