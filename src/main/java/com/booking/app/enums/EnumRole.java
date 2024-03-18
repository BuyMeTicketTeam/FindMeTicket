package com.booking.app.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Set;

public enum EnumRole {

    ADMIN("ADMIN"),
    USER("USER");

    private final String role;

    EnumRole(String role) {
        this.role = role;
    }

    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

}
