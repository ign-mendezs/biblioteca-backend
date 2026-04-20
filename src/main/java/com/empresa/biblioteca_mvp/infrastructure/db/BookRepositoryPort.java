package com.empresa.biblioteca_mvp.infrastructure.db;

import com.empresa.biblioteca_mvp.domain.model.Book;
import com.empresa.biblioteca_mvp.domain.port.BookRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class BookRepositoryAdapter implements BookRepositoryPort {

    private final SpringDataBookRepository jpaRepository;

    @Override
    public Book save(Book book) {
        BookEntity entity = jpaRepository.findById(book.getId())
                .orElseGet(BookEntity::new);

        entity.setId(book.getId());
        entity.setTitle(book.getTitle());
        entity.setAuthor(book.getAuthor());
        entity.setStatus(book.getStatus());
        entity.setActiveUserId(book.getActiveUserId());
        entity.setEndTime(book.getEndTime());

        BookEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Book> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    private Book toDomain(BookEntity entity) {
        return Book.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .status(entity.getStatus())
                .activeUserId(entity.getActiveUserId())
                .endTime(entity.getEndTime())
                .build();
    }
}