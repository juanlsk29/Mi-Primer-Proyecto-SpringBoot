package com.CineReview.CineReview.service;

import com.CineReview.CineReview.model.User;
import com.CineReview.CineReview.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Valida las reglas de negocio del registro (usuario/correo únicos) y,
     * si todo está correcto, cifra la contraseña y guarda al usuario.
     *
     * @return true si el usuario se registró con éxito, false si hubo errores
     *         (los errores quedan cargados en el BindingResult).
     */
    public boolean registrarUsuario(User user, BindingResult result) {

        if (userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Ese nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Ese correo ya está registrado");
        }

        if (result.hasErrors()) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return true;
    }
}
