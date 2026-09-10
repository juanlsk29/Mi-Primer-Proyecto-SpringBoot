package com.CineReview.CineReview.service;

import com.CineReview.CineReview.model.Review;
import com.CineReview.CineReview.model.User;
import com.CineReview.CineReview.repository.ReviewRepository;
import com.CineReview.CineReview.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    /**
     * Devuelve todas las reseñas ordenadas de la más reciente a la más antigua.
     */
    public List<Review> obtenerTodas() {
        return reviewRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Asigna la reseña al usuario autenticado (por su username) y la guarda.
     */
    public Review publicarReview(Review review, String username) {
        User autor = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));

        review.setUser(autor);
        return reviewRepository.save(review);
    }
}
