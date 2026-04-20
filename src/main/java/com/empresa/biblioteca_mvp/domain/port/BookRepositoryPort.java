package com.empresa.biblioteca_mvp.domain.port;

import com.empresa.biblioteca_mvp.domain.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepositoryPort {
    Book save(Book book);
    Optional<Book> findById(String id);
    List<Book> findAll();
    void deleteById(String id);
}