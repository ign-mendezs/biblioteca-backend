// src/main/java/com/empresa/biblioteca_mvp/infrastructure/adapter/in/web/BookManagementController.java
package com.empresa.biblioteca_mvp.presenters.controller;

import com.empresa.biblioteca_mvp.application.BookManagement;
import com.empresa.biblioteca_mvp.domain.model.Book;
import com.empresa.biblioteca_mvp.presenters.dto.BookResponse;
import com.empresa.biblioteca_mvp.presenters.dto.CreateBookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/management/books")
@RequiredArgsConstructor
public class BookManagementController {

    private final BookManagement management;

    @GetMapping
    public List<BookResponse> getAllBooks() {
        return management.getAllBooks().stream()
                .map(BookResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest request) {
        Book book = management.createBook(request.id(), request.title(), request.author());
        return BookResponse.fromDomain(book);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable String id, @Valid @RequestBody CreateBookRequest request) {
        Book book = management.updateBook(id, request.title(), request.author());
        return BookResponse.fromDomain(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        management.removeBook(id);
    }
}