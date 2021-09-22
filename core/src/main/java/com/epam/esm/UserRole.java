package com.epam.esm;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public enum UserRole {
    ADMIN(List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"))),
    USER(List.of(new SimpleGrantedAuthority("ROLE_USER"))),
    GUEST(null);

    private final List<SimpleGrantedAuthority> authorities;

    UserRole(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public List<SimpleGrantedAuthority> authorities() {
        return authorities;
    }

}