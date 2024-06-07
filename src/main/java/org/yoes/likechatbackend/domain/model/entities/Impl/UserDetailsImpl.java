package org.yoes.likechatbackend.domain.model.entities.Impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.yoes.likechatbackend.domain.model.entities.User;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // O cualquier otro campo que uses como nombre de usuario
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Cambiado a true para indicar que la cuenta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isEnabled(); // Verifica si la cuenta está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Cambiado a true para indicar que las credenciales nunca expiran
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // Verifica si la cuenta está habilitada
    }
}
