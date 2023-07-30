package dev.ixale.ecommerceservice.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Authority implements GrantedAuthority{

    DEFAULT("DEFAULT"),
    READ("READ"),
    WRITE("WRITE"),
    USER("USER"),
    ADMIN("ADMIN");

    private final String authority;

    Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return this.authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static Authority toAuthority(String authority) {
        for (Authority a : Authority.values()) {
            if (a.getAuthority().equals(authority)) {
                return a;
            }
        }
        return Authority.DEFAULT;
    }

    public static Set<Authority> toAuthority(String authorities, String delimiter) {
        return Arrays.stream(authorities.split(delimiter))
                .map(Authority::toAuthority).collect(Collectors.toSet());
    }

    public static String fromAuthority(Set<Authority> authorities, String delimiter) {
        return authorities.stream()
                .map(Authority::getAuthority).collect(Collectors.joining(delimiter));
    }
}
