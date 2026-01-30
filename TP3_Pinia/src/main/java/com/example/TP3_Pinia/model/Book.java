package com.example.TP3_Pinia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 200,min = 2, message = "Le titre doit contenir entre 2 et 200 caractères")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "L'auteur est obligatoire")
    @Size(max = 100,min =2, message = "Le nom de l'auteur ne peut pas dépasser 100 caractères")
    @Column(nullable = false,length = 100)
    private String author;

    @NotBlank(message = "L'ISBN est obligatoire")
    @Pattern(regexp = "^978-\\d{10}$", message = "Format ISBN invalide (978-XXXXXXXXXX)")
    @Column(nullable = false, unique = true, length = 14)
    private String isbn;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    @Column(nullable = false)
    private double price;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column()
    private BookCategory category;

    @NotNull(message = "L'année de publication est obligatoire")
    @Column(nullable = false)
    private Integer publicationYear;

    @Column
    private String coverUrl;

}
