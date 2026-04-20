package com.empresa.biblioteca_mvp.infrastructure.config;

import com.empresa.biblioteca_mvp.application.BookManagement;
import com.empresa.biblioteca_mvp.application.BookOperations;
import com.empresa.biblioteca_mvp.domain.port.BookRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    // Spring buscará automáticamente una implementación de BookRepositoryPort 
    // (nuestro BookRepositoryAdapter) y la inyectará aquí.

    @Bean
    public BookManagement bookManagement(BookRepositoryPort bookRepositoryPort) {
        return new BookManagement(bookRepositoryPort);
    }

    @Bean
    public BookOperations bookOperations(BookRepositoryPort bookRepositoryPort) {
        return new BookOperations(bookRepositoryPort);
    }
}