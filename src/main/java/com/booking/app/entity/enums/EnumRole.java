package com.booking.app.entity.enums;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public enum EnumRole {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    EnumRole(String role) {
        this.role = role;
    }

    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

}
