package io.github.artsobol.bookcrossing.feature.refreshtoken.entity;

import io.github.artsobol.bookcrossing.feature.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@Table(name = "refresh_tokens", indexes = {
        @Index(name = "idx_refresh_token_user_id", columnList = "user_id"),
        @Index(name = "idx_refresh_token_session_id", columnList = "session_id"),
        @Index(name = "idx_refresh_token_token_hash", columnList = "token_hash")
})
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "token_hash", nullable = false, unique = true) private String tokenHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_refresh_token_user_id"))
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "revoked_reason")
    private RevokedReason revokedReason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replace_by_id", unique = true)
    private RefreshToken replaceBy;

    @OneToOne(mappedBy = "replaceBy")
    private RefreshToken replacedToken;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @NotBlank
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @NotBlank
    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(name = "device_name")
    private String deviceName;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "last_used_at", nullable = false)
    private Instant lastUsedAt;

}
