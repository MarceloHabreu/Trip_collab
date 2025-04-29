package io.github.marcelohabreu.tripCollab.config.security;

import io.github.marcelohabreu.tripCollab.repositories.BlacklistedTokenRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final JwtDecoder nimbusJwtDecoder;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public CustomJwtDecoder(JwtDecoder nimbusJwtDecoder, BlacklistedTokenRepository blacklistedTokenRepository) {
        this.nimbusJwtDecoder = nimbusJwtDecoder;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        if (blacklistedTokenRepository.existsByToken(token)) {
            throw new JwtException("Token is blacklisted");
        }
        return nimbusJwtDecoder.decode(token);
    }
}