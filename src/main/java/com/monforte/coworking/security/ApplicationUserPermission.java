package com.monforte.coworking.security;

public enum ApplicationUserPermission {
    ROOM_READ("room:read"),
    ROOM_WRITE("room:write"),
    COMPANY_READ("company:read"),
    COMPANY_WRITE("company:write"),
    RESERVATION_READ("reservation:read"),
    RESERVATION_WRITE("reservation:write");

    private final String permission;

    ApplicationUserPermission(String permission){
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
