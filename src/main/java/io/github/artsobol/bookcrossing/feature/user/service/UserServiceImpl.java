package io.github.artsobol.bookcrossing.feature.user.service;

import io.github.artsobol.bookcrossing.exception.http.ConflictException;
import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.role.service.RoleService;
import io.github.artsobol.bookcrossing.feature.user.dto.request.CreateUserRequest;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import io.github.artsobol.bookcrossing.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserFinder {

    private final RoleService roleService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        log.info("Creating user with username: {}", request.username());
        ensureUniqueUsername(request.username());
        ensureUniqueEmail(request.email());

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(request.passwordHash())
                .roles(Set.of(roleService.findByName("USER")))
                .build();
        userRepository.save(user);

        log.info("User created with username: {}", user.getUsername());
        return user;
    }

    @Override
    public User findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("user.not.found")
        );
    }

    @Override
    public User findById(UUID userId) {
        log.debug("Finding user by id: {}", userId);
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user.not.found"));
    }

    private void ensureUniqueEmail(String email) {
        log.debug("Checking if email: {} is unique", email);
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("user.email.exists");
        }
    }

    private void ensureUniqueUsername(String username) {
        log.debug("Checking if username: {} is unique", username);
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("user.username.exists");
        }
    }
}
