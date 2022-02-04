package com.monforte.coworking.security;

import com.google.common.collect.Sets;

import java.util.Set;

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
}
