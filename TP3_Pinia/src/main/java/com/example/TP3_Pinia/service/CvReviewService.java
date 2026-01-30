package com.example.TP3_Pinia.service;

import com.example.TP3_Pinia.model.CvReview;
import com.example.TP3_Pinia.repository.CvReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CvReviewService {

    private final CvReviewRepository cvReviewRepository;

    public List<CvReview> findAll() {
        return cvReviewRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<CvReview> findById(Long id) {
        return cvReviewRepository.findById(id);
    }

    public CvReview save(CvReview review) {
        return cvReviewRepository.save(review);
    }

    public boolean delete(Long id) {
        if (cvReviewRepository.existsById(id)) {
            cvReviewRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Double getAverageRating() {
        Double avg = cvReviewRepository.getAverageRating();
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public long getReviewCount() {
        return cvReviewRepository.count();
    }
}
