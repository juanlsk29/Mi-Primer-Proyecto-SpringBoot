package com.CineReview.CineReview.controller;

import com.CineReview.CineReview.model.Review;
import com.CineReview.CineReview.service.ReviewService;
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

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("reviews", reviewService.obtenerTodas());
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
            model.addAttribute("reviews", reviewService.obtenerTodas());
            model.addAttribute("username", authentication.getName());
            return "home";
        }

        // El controlador ya no busca al usuario ni arma la relación:
        // se lo delega a ReviewService.
        reviewService.publicarReview(review, authentication.getName());

        return "redirect:/";
    }
}
