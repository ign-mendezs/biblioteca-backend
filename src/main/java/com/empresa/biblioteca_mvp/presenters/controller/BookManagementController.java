// src/main/java/com/empresa/biblioteca_mvp/infrastructure/adapter/in/web/BookManagementController.java
package com.empresa.biblioteca_mvp.presenters.controller;

import com.empresa.biblioteca_mvp.application.BookManagement;
import com.empresa.biblioteca_mvp.domain.model.Book;
import com.empresa.biblioteca_mvp.presenters.dto.AdminBookResponse;
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
    public List<AdminBookResponse> getAllBooks() {
        return management.getAllBooks().stream()
                .map(AdminBookResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdminBookResponse createBook(@Valid @RequestBody CreateBookRequest request) {
        Book book = management.createBook(request.id(), request.title(), request.author());
        return AdminBookResponse.fromDomain(book);
    }

    @PutMapping("/{id}")
    public AdminBookResponse updateBook(@PathVariable String id, @Valid @RequestBody CreateBookRequest request) {
        Book book = management.updateBook(id, request.title(), request.author());
        return AdminBookResponse.fromDomain(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        management.removeBook(id);
    }
}