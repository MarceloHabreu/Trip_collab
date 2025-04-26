package io.github.marcelohabreu.tripCollab.services;

import io.github.marcelohabreu.tripCollab.dtos.user.SimpleUserResponse;
import io.github.marcelohabreu.tripCollab.entities.Follower;
import io.github.marcelohabreu.tripCollab.entities.User;
import io.github.marcelohabreu.tripCollab.entities.compostiteKey.FollowerId;
import io.github.marcelohabreu.tripCollab.exceptions.user.follower.AlreadyFollowingException;
import io.github.marcelohabreu.tripCollab.exceptions.user.follower.NotFollowingException;
import io.github.marcelohabreu.tripCollab.exceptions.user.follower.SelfFollowException;
import io.github.marcelohabreu.tripCollab.exceptions.user.CustomAccessDeniedException;
import io.github.marcelohabreu.tripCollab.exceptions.user.UserNotFoundException;
import io.github.marcelohabreu.tripCollab.exceptions.user.follower.SelfUnfollowException;
import io.github.marcelohabreu.tripCollab.repositories.FollowerRepository;
import io.github.marcelohabreu.tripCollab.repositories.UserRepository;
import io.github.marcelohabreu.tripCollab.utils.AuthUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FollowerService {
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final FollowerRepository followerRepository;

    public FollowerService(UserRepository userRepository, AuthUtil authUtil, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.followerRepository = followerRepository;
    }

    private record UserPair(User follower, User followed) {}
    // pegar seguidor e seguido
    private UserPair getUsers(UUID userId, Principal authenticatedUser) {
        // Validar user autenticado
        UUID followerId;
        try {
            followerId = UUID.fromString(authenticatedUser.getName());
        } catch (IllegalArgumentException e){
            throw new CustomAccessDeniedException("Invalid token format: user ID must be a valid UUID");
        }

        // buscar users
        User userFollowed = userRepository.findById(userId).orElseThrow(UserNotFoundException::new); // quem vai ser seguido
        User userFollower = userRepository.findById(followerId).orElseThrow(UserNotFoundException::new); // quem vai seguir

        return new UserPair(userFollower, userFollowed);
    }

    @Transactional
    public ResponseEntity<Void> followUser(UUID userId, Principal authenticatedUser) {
        var users = getUsers(userId, authenticatedUser);

        User userFollowing = users.follower(); // quem vai seguir
        User userFollowed = users.followed(); // quem vai ser seguido

        // Impedir de se auto-seguir
        if (userId.equals(userFollowing.getUserId())){
            throw new SelfFollowException();
        }

        // Verificar se ja segue
        FollowerId followerId = new FollowerId(userFollowing.getUserId(), userFollowed.getUserId());
        Optional<Follower> existingFollow = followerRepository.findById(followerId);
        if (existingFollow.isPresent()) {
            throw new AlreadyFollowingException();
        }

        // atualizar os contadores
        userFollowing.setFollowingCount(userFollowing.getFollowingCount() + 1);
        userFollowed.setFollowerCount(userFollowed.getFollowerCount() + 1);
        userRepository.save(userFollowing);
        userRepository.save(userFollowed);

        Follower newFollow = new Follower();
        newFollow.setId(followerId);
        newFollow.setFollower(userFollowing);
        newFollow.setFollowed(userFollowed);

        followerRepository.save(newFollow);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Transactional
    public ResponseEntity<Void> unfollowUser(UUID userId, Principal authenticatedUser) {
        var users = getUsers(userId, authenticatedUser);

        User userFollowing = users.follower(); // quem vai seguir
        User userFollowed = users.followed(); // quem vai ser seguido

        // Impedir da tentativa de dar unfollow em si mesma
        if (userId.equals(userFollowing.getUserId())){
            throw new SelfUnfollowException();
        }

        // Verificar se segue
        FollowerId followerId = new FollowerId(userFollowing.getUserId(), userFollowed.getUserId());
        Optional<Follower> existingFollow = followerRepository.findById(followerId);
        if (existingFollow.isEmpty()) {
            throw new NotFollowingException();
        }

        // atualizar os contadores
        userFollowing.setFollowingCount(userFollowing.getFollowingCount() - 1);
        userFollowed.setFollowerCount(userFollowed.getFollowerCount() - 1);
        userRepository.save(userFollowing);
        userRepository.save(userFollowed);

        followerRepository.deleteById(followerId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<SimpleUserResponse>> getFollowers(UUID userId,Principal principal, Pageable pageable) {
        authUtil.checkUserExists(userId);
        User authenticatedUser = principal != null ? userRepository.findById(UUID.fromString(principal.getName())).orElseThrow(UserNotFoundException::new) :
                null;

        List<SimpleUserResponse> followers = followerRepository.findByFollowed_UserId(userId, pageable)
                .stream()
                .map(follower -> SimpleUserResponse.fromModel(follower.getFollower(), authenticatedUser, followerRepository))
                .toList();

        return ResponseEntity.ok(followers);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<SimpleUserResponse>> getFollowing(UUID userId, Principal principal, Pageable pageable) {
        authUtil.checkUserExists(userId);
        User authenticatedUser = principal != null ? userRepository.findById(UUID.fromString(principal.getName())).orElseThrow(UserNotFoundException::new) :
                null;

        List<SimpleUserResponse> following = followerRepository.findByFollower_UserId(userId, pageable)
                .stream()
                .map(follower -> SimpleUserResponse.fromModel(follower.getFollowed(), authenticatedUser, followerRepository))
                .toList();

        return ResponseEntity.ok(following);
    }



}
