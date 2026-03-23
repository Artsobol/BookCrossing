package io.github.artsobol.bookcrossing.feature.book.entity;

import io.github.artsobol.bookcrossing.feature.author.entity.Author;
import io.github.artsobol.bookcrossing.feature.genre.entity.Genre;
import io.github.artsobol.bookcrossing.feature.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @NotBlank
    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Getter
    @Column(name = "description")
    private String description;

    @Getter
    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    private Genre genre;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public static Book create(String title, String description, User user) {
        Book book = new Book();
        book.user = Objects.requireNonNull(user, "User is null");
        book.status = BookStatus.AVAILABLE;
        book.changeTitle(title);
        book.changeDescription(description);
        return book;
    }

    public void changeTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalStateException("Title is blank");
        }
        this.title = title;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeUser(User user) {
        this.user = Objects.requireNonNull(user, "User is null");
    }

    public void changeAuthor(Author author) {
        this.author = Objects.requireNonNull(author, "Author is null");
    }

    public void changeGenre(Genre genre) {
        this.genre = Objects.requireNonNull(genre, "Genre is null");
    }
}
