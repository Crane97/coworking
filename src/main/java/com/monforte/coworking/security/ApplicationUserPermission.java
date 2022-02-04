package com.monforte.coworking.security;

public enum ApplicationUserPermission {
    ROOM_READ("room:read"),
    ROOM_WRITE("room:write"),
    COMPANY_READ("company:read"),
    COMPANY_WRITE("company:write"),
    RESERVATION_READ("reservation:read"),
    RESERVATION_WRITE("reservation:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    // A ROLE CAN HAVE 0..* PERMISSIONS

    private final String permission;

    ApplicationUserPermission(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
