package com.example.TP3_Pinia.controller;

import com.example.TP3_Pinia.model.CvReview;
import com.example.TP3_Pinia.service.CvReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cv-reviews")
@RequiredArgsConstructor
public class CvReviewController {

    private final CvReviewService cvReviewService;

    // =========================
    // GET /api/cv-reviews (public)
    // Récupérer tous les avis
    // =========================
    @GetMapping
    public ResponseEntity<List<CvReview>> getAllReviews() {
        return ResponseEntity.ok(cvReviewService.findAll());
    }

    // =========================
    // GET /api/cv-reviews/stats (public)
    // Récupérer les statistiques (moyenne, nombre d'avis)
    // =========================
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", cvReviewService.getAverageRating());
        stats.put("totalReviews", cvReviewService.getReviewCount());
        return ResponseEntity.ok(stats);
    }

    // =========================
    // GET /api/cv-reviews/{id} (public)
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<CvReview> getReviewById(@PathVariable Long id) {
        return cvReviewService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // =========================
    // POST /api/cv-reviews (public)
    // Créer un nouvel avis
    // =========================
    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody CvReview review) {
        CvReview created = cvReviewService.save(review);
        return ResponseEntity
                .created(URI.create("/api/cv-reviews/" + created.getId()))
                .body(created);
    }

    // =========================
    // DELETE /api/cv-reviews/{id} (admin only)
    // Supprimer un avis
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        if (cvReviewService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
