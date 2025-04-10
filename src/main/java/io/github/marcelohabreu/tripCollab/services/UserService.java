package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.dtos.user.AdminUserResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.PublicUserResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.UserUpdateRequest;
import io.github.marcelohabreu.tripCollab.dtos.user.UserResponse;
import io.github.marcelohabreu.tripCollab.entities.User;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.EmailAlreadyExistsException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UsernameAlreadyExistsException;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import io.github.marcelohabreu.tripCollab.utils.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;


@Service
@Validated
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final AuthUtil authUtil;

    public UserService(UserRepository repository, BCryptPasswordEncoder encoder, AuthUtil authUtil) {
        this.repository = repository;
        this.encoder = encoder;
        this.authUtil = authUtil;
    }

    // Extract the id from token
    private static UUID extractId(JwtAuthenticationToken token) {
        return UUID.fromString(token.getName());
    }

    public ResponseEntity<List<AdminUserResponse>> listAdminUsers() {
        return ResponseEntity.ok(repository.findAll().stream().sorted(Comparator.comparing(User::getUsername)).map(AdminUserResponse::fromModel).toList());
    }

    public ResponseEntity<List<PublicUserResponse>> listPublicUsers() {
        return ResponseEntity.ok(repository.findAll().stream().sorted(Comparator.comparing(User::getUsername)).map(PublicUserResponse::fromModel).toList());
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> updateMyProfile(JwtAuthenticationToken token, UserUpdateRequest dto) throws CustomAccessDeniedException {
        var userId = extractId(token);
        var user = repository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (dto.username() != null && !dto.username().isBlank()) {
            var existingUser = repository.findByUsername(dto.username());
            if (existingUser.isPresent() && !existingUser.get().getUserId().equals(userId)) {
                throw new UsernameAlreadyExistsException();
            }
            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().isBlank()) {
            var existingEmail = repository.findByEmail(dto.email());
            if (existingEmail.isPresent() && !existingEmail.get().getUserId().equals(userId)) {
                throw new EmailAlreadyExistsException();
            }
            user.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(encoder.encode(dto.password()));
        }

        repository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Profile successfully updated");
        response.put("user", UserResponse.fromModel(user));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<UserResponse> getMyProfile(JwtAuthenticationToken token) {
        UUID userId = extractId(token);
        var user = repository.findById(userId).map(UserResponse::fromModel).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public ResponseEntity<PublicUserResponse> getPublicUser(UUID id) {
        var user = repository.findById(id).map(PublicUserResponse::fromModel).orElseThrow(UserNotFoundException::new);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Transactional
    public ResponseEntity<Void> deleteMyProfile(JwtAuthenticationToken token) throws CustomAccessDeniedException {
        var userId = extractId(token);
        var user = repository.findById(userId).orElseThrow(UserNotFoundException::new);
        repository.delete(user);
        return ResponseEntity.noContent().build();
    }
}
