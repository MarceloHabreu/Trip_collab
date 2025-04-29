package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.dtos.auth.*;
import io.github.marcelohabreu.tripCollab.entities.BlacklistedToken;
import io.github.marcelohabreu.tripCollab.entities.Role;
import io.github.marcelohabreu.tripCollab.entities.User;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomBadCredentialsException;
import io.github.marcelohabreu.tripCollab.exceptions.user.EmailAlreadyExistsException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UsernameAlreadyExistsException;
import io.github.marcelohabreu.tripCollab.repositories.BlacklistedTokenRepository;
import io.github.marcelohabreu.tripCollab.repositories.RoleRepository;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/tripcollab/auth")
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, BlacklistedTokenRepository blacklistedTokenRepository) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    private TokenDetails generateToken(User user) {
        var now = Instant.now();
        var expiresIn = 86400L; // 24 horas

        var scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("backend_TripCollab")
                .subject(user.getUserId().toString())
                .audience(List.of("tripcollab-api"))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .id(UUID.randomUUID().toString())
                .claim("scope", scopes)
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenDetails(jwtValue, expiresIn);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.email());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new CustomBadCredentialsException();
        }

        TokenDetails tokenDetails = generateToken(user.get());
        return ResponseEntity.ok(new AuthResponse("Successful login", tokenDetails.token(), tokenDetails.expiresIn()));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(JwtAuthenticationToken token) {
        userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(UserNotFoundException::new);

        // Extrair o token JWT
        String jwt = token.getToken().getTokenValue();
        // Obter a expiração do token
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                token.getToken().getExpiresAt(), java.time.ZoneId.systemDefault()
        );

        // Adicionar à blacklist
        blacklistedTokenRepository.save(new BlacklistedToken(jwt, expiresAt));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        var userRole = roleRepository.findByName(Role.Values.USER.name()).orElseThrow(() -> new RuntimeException("Role USER not found"));

        var userEmailFromDb = userRepository.findByEmail(registerRequest.email());
        var usernameFromDb = userRepository.findByUsername(registerRequest.username());
        if (usernameFromDb.isPresent()) {
            throw new UsernameAlreadyExistsException();

        } else if (userEmailFromDb.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        var newUser = new User();
        newUser.setUsername(registerRequest.username());
        newUser.setEmail(registerRequest.email());
        newUser.setPassword(passwordEncoder.encode(registerRequest.password()));
        newUser.setRoles(Set.of(userRole));

        userRepository.save(newUser);

        TokenDetails tokenDetails = generateToken(newUser);

        // Criando uri absoluta para o response entity
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/tripcollab/users/{id}")
                .buildAndExpand(newUser.getUserId())
                .toUri();

        return ResponseEntity.created(location).body(new AuthResponse("Successful registration", tokenDetails.token(), tokenDetails.expiresIn()));
    }
}
