package com.CineReview.CineReview.controller;

import com.CineReview.CineReview.model.User;
import com.CineReview.CineReview.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                            BindingResult result) {

        // El controlador ya no valida duplicados ni cifra la contraseña:
        // toda esa lógica de negocio vive ahora en UserService.
        boolean exito = userService.registrarUsuario(user, result);

        if (!exito) {
            return "register";
        }

        return "redirect:/login?registered";
    }
}
