package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.common.Utils;
import dev.ixale.ecommerceservice.exception.FailedOperationException;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService{
    private final JwtEncoder jwtEncoder;

    private static final Logger LOGGER = Utils.getLogger(TokenServiceImpl.class);

    public TokenServiceImpl(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String generateJwt(Authentication authentication) throws FailedOperationException {
        try {

        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("username", authentication.getName())
                .build();

        LOGGER.debug("\n" +
                "Token Requested for user: " + authentication.getName() +
                "at: " + now +
                ", with credentials: " + authentication.getCredentials() +
                ", with scope: " + scope);

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        } catch (Exception e) {
            LOGGER.error("Error generating token.", e);
            throw new FailedOperationException("Error generating token");
        }
    }
}
