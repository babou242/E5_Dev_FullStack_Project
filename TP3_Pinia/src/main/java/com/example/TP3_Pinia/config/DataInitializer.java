package com.example.TP3_Pinia.config;

import com.example.TP3_Pinia.model.*;
import com.example.TP3_Pinia.repository.BookRepository;
import com.example.TP3_Pinia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // =========================
        // USERS
        // =========================
        if (userRepository.count() == 0) {

            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build());

            userRepository.save(User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.ROLE_USER)
                    .build());
        }

        // =========================
        // BOOKS
        // =========================
        if (bookRepository.count() == 0) {

            bookRepository.save(Book.builder()
                    .title("Les Misérables")
                    .author("Victor Hugo")
                    .isbn("978-1234567890")
                    .price(19.99)
                    .description("Roman classique de la littérature française.")
                    .category(BookCategory.ROMAN)
                    .publicationYear(1862)
                    .build());

            bookRepository.save(Book.builder()
                    .title("Les Fleurs du Mal")
                    .author("Charles Baudelaire")
                    .isbn("978-1234567891")
                    .price(14.50)
                    .publicationYear(1857)
                    .category(BookCategory.POESIE)
                    .build());
        }
    }
}

