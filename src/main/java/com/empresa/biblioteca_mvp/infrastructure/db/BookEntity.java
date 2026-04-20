package com.empresa.biblioteca_mvp.infrastructure.db;

import com.empresa.biblioteca_mvp.domain.model.BookStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    private String activeUserId;

    private LocalDateTime endTime;

    @Version
    private Long version;
}