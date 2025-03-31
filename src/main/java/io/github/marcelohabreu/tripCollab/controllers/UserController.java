package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.dtos.user.PublicUserResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.UserUpdateRequest;
import io.github.marcelohabreu.tripCollab.dtos.user.AdminUserResponse;
import io.github.marcelohabreu.tripCollab.dtos.user.UserResponse;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tripcollab/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<AdminUserResponse>> listAdminUsers(){
        return service.listAdminUsers();
    }

    @GetMapping("/public")
    public ResponseEntity<List<PublicUserResponse>> listPublicUsers(){
        return service.listPublicUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getMyProfile(@PathVariable UUID id, JwtAuthenticationToken token){
        return service.getMyProfile(id, token);
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<PublicUserResponse> getUser(@PathVariable UUID id){
        return service.getPublicUser(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMyProfile(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest dto, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.updateMyProfile(id, token, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyProfile(@PathVariable UUID id, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.deleteMyProfile(id, token);
    }
}
