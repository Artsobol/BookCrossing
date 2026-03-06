package io.github.artsobol.bookcrossing.feature.user.service;

import io.github.artsobol.bookcrossing.exception.http.ConflictException;
import io.github.artsobol.bookcrossing.exception.http.NotFoundException;
import io.github.artsobol.bookcrossing.feature.role.service.RoleService;
import io.github.artsobol.bookcrossing.feature.user.dto.request.CreateUserRequest;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import io.github.artsobol.bookcrossing.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleService roleService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        ensureUniqueUsername(request.username());
        ensureUniqueEmail(request.email());

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(request.passwordHash())
                .roles(Set.of(roleService.findByName("USER")))
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("user.not.found")
        );
    }

    private void ensureUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("user.email.exists");
        }
    }

    private void ensureUniqueUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new ConflictException("user.username.exists");
        }
    }
}
