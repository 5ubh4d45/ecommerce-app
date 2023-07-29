package dev.ixale.ecommerceservice.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority {

    READ("READ"),
    WRITE("WRITE"),
    USER("USER"),
    ADMIN("ADMIN");

    private final String authority;

    UserAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public String toString() {
        return this.authority;
    }
}
