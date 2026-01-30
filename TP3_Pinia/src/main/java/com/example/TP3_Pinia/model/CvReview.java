package com.example.TP3_Pinia.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cv_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CvReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, min = 2, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String authorName;

    @Email(message = "Format email invalide")
    @Size(max = 150)
    @Column(length = 150)
    private String email;

    @NotBlank(message = "Le commentaire est obligatoire")
    @Size(max = 1000, min = 10, message = "Le commentaire doit contenir entre 10 et 1000 caractères")
    @Column(nullable = false, length = 1000)
    private String comment;

    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note minimum est 1")
    @Max(value = 5, message = "La note maximum est 5")
    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
