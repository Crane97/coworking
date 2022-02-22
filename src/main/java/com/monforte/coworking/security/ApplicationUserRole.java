package com.monforte.coworking.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.monforte.coworking.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    ADMIN(
            Sets.newHashSet(
                    ROOM_READ,
                    ROOM_WRITE,
                    COMPANY_READ,
                    COMPANY_WRITE,
                    RESERVATION_READ,
                    RESERVATION_WRITE,
                    USER_READ,
                    USER_WRITE
            )
    ),

    PARTNER(
            Sets.newHashSet(
                    ROOM_READ,
                    COMPANY_READ,
                    COMPANY_WRITE,
                    RESERVATION_READ,
                    RESERVATION_WRITE
            )
    );

    private final Set<ApplicationUserPermission> permissions;


    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions(){
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
