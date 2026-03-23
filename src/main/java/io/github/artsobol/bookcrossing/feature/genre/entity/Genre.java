package io.github.artsobol.bookcrossing.feature.genre.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "genres")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Genre {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "title", nullable = false, length = 64)
    private String title;

    @Getter
    @Column(name = "description")
    private String description;

    @Getter
    @Column(name = "slug", unique = true, nullable = false, length = 128)
    private String slug;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private UUID createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private UUID updatedBy;

    public static Genre create(String title, String description, String slug) {
        Genre entity = new Genre();
        entity.updateTitle(title);
        entity.updateDescription(description);
        entity.updateSlug(slug);
        return entity;
    }

    public void updateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalStateException("Title is blank");
        }
        this.title = title;
    }

    public void updateSlug(String slug) {
        if (slug == null || slug.isBlank()) {
            throw new IllegalStateException("Slug is blank");
        }
        this.slug = slug;
    }

    public void updateDescription(String description) {
        this.description = description;
    }
}
