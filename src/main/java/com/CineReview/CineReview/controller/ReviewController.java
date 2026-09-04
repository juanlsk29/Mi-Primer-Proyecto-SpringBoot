package com.cinereview.cinereview.controller;

import com.cinereview.cinereview.model.Review;
import com.cinereview.cinereview.model.User;
import com.cinereview.cinereview.repository.ReviewRepository;
import com.cinereview.cinereview.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewController(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("reviews", reviewRepository.findAllByOrderByCreatedAtDesc());
        model.addAttribute("review", new Review());
        model.addAttribute("username", authentication.getName());
        return "home";
    }

    @PostMapping("/reviews")
    public String createReview(@Valid @ModelAttribute("review") Review review,
                                BindingResult result,
                                Model model,
                                Authentication authentication) {

        if (result.hasErrors()) {
            model.addAttribute("reviews", reviewRepository.findAllByOrderByCreatedAtDesc());
            model.addAttribute("username", authentication.getName());
            return "home";
        }

        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));

        review.setUser(currentUser);
        reviewRepository.save(review);

        return "redirect:/";
    }
}
