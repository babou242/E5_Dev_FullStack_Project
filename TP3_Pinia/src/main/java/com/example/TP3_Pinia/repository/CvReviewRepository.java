package com.example.TP3_Pinia.repository;

import com.example.TP3_Pinia.model.CvReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CvReviewRepository extends JpaRepository<CvReview, Long> {
    
    // Récupérer tous les avis triés par date (plus récent en premier)
    List<CvReview> findAllByOrderByCreatedAtDesc();
    
    // Calculer la moyenne des notes
    @Query("SELECT AVG(r.rating) FROM CvReview r")
    Double getAverageRating();
    
    // Compter le nombre total d'avis
    long count();
}
