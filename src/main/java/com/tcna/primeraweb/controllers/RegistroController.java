package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.Role;
import com.tcna.primeraweb.models.User;
import com.tcna.primeraweb.repositories.RoleRepository;
import com.tcna.primeraweb.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

@Controller
@RequestMapping("/registro")
@RequiredArgsConstructor
public class RegistroController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("user", new User());
        return "registro";
    }

    @PostMapping
    public String registrarUsuario(@Valid User user, BindingResult bindingResult, Model model) {
        // Verificar si el username ya existe
        if (userRepository.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "El nombre de usuario ya está en uso");
        }

        if (bindingResult.hasErrors()) {
            return "registro";
        }

        // Asignar el rol USER por defecto
        Role userRole = roleRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userRepository.save(user);
        return "redirect:/login?registroExitoso";
    }

}