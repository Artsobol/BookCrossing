package io.github.artsobol.bookcrossing.feature.user.entity;

import io.github.artsobol.bookcrossing.feature.role.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Getter
    @NotBlank
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Getter
    @NotBlank
    @Column(name = "username", unique = true, nullable = false, length = 64)
    private String username;

    @Email
    @Getter
    @NotBlank
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public static User create(String username, String email, String passwordHash) {
        User entity = new User();
        entity.changeUsername(username);
        entity.changeEmail(email);
        entity.changePasswordHash(passwordHash);
        return entity;
    }

    public void addRole(Role role) {
        ensureRoleNotNull(role);
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        ensureRoleNotNull(role);
        if (!roles.contains(role)) {
            throw new IllegalArgumentException("User with id: " + this.id + " has no role: " + role);
        }
        this.roles.remove(role);
    }

    private static void ensureRoleNotNull(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role is null");
        }
    }

    private void changeUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username is blank");
        }
        this.username = username;
    }

    private void changeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is null");
        }
        this.email = email;
    }

    private void changePasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("PasswordHash is null");
        }
        this.passwordHash = passwordHash;
    }
}
