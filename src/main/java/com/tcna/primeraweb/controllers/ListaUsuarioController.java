package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.User;
import com.tcna.primeraweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class ListaUsuarioController {

    private final UserRepository userRepository;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", userRepository.findAll());
        return "usuario/listado"; //archivo html
    }

    @PostMapping("/cambiar-estado/{id}")
    public String cambiarEstadoUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User usuario = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Cambiar el estado
        usuario.setEnabled(!usuario.isEnabled());
        userRepository.save(usuario);

        redirectAttributes.addFlashAttribute("mensaje",
                "Estado del usuario " + usuario.getUsername() + " cambiado exitosamente");

        return "redirect:/usuarios";
    }

}