package io.github.marcelohabreu.tripCollab.controllers;

import io.github.marcelohabreu.tripCollab.dtos.UpdateUserDto;
import io.github.marcelohabreu.tripCollab.dtos.UserDetailsResponse;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/tripcollab/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserDetailsResponse>> list(){
        return service.listAllUsers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateMe(@PathVariable UUID id, @RequestBody UpdateUserDto dto, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.updateUser(id, token, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMe(@PathVariable UUID id, JwtAuthenticationToken token) throws CustomAccessDeniedException {
        return service.deleteUser(id, token);
    }
}
