package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.dtos.UserDetailsResponse;
import io.github.marcelohabreu.tripCollab.dtos.UpdateUserDto;
import io.github.marcelohabreu.tripCollab.entities.User;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;


    public UserService(UserRepository repository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void checkOwnership(JwtAuthenticationToken token, UUID id) throws CustomAccessDeniedException {
        if (token == null) {
            throw new CustomAccessDeniedException("Authentication required!");
        }
        UUID authenticatedUserId = UUID.fromString(token.getName());
        if (!authenticatedUserId.equals(id)) {
            throw new CustomAccessDeniedException("You can only make changes on your own account! Please try again");
        }
    }

    public ResponseEntity<List<UserDetailsResponse>> listAllUsers() {
        List<UserDetailsResponse> users = repository.findAll().stream().sorted(Comparator.comparing(User::getUsername)).map(UserDetailsResponse::fromModel).toList();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Transactional
    public ResponseEntity<Map<String, String>> updateUser(UUID id, JwtAuthenticationToken token, UpdateUserDto dto) throws CustomAccessDeniedException {
        var user = repository.findById(id).orElseThrow(UserNotFoundException::new);
        checkOwnership(token, id);

        if (dto.username() != null && !dto.username().isBlank()) {
            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().isBlank()){
            user.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()){
            user.setPassword(encoder.encode(dto.password()));
        }

        repository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile successfully updated");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<Map<String, String>> deleteUser(UUID id, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        var user = repository.findById(id).orElseThrow(UserNotFoundException::new);
        checkOwnership(token, id);

        repository.delete(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Profile successfully deleted");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
