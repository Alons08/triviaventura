package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.User;
import com.tcna.primeraweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String cambiarEstadoUsuario(@PathVariable Long id,
                                       RedirectAttributes redirectAttributes,
                                       Authentication authentication) {
        try {
            User usuario = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); //si pongo un id que no es esto lanza el mensaje de error de SpringBoot

            // Verificar si el usuario está intentando desactivarse a sí mismo
            String currentUsername = authentication.getName();
            if(usuario.getUsername().equals(currentUsername)) {
                redirectAttributes.addFlashAttribute("error",
                        "No puedes cambiar tu propio estado");
                return "redirect:/usuarios";
            }

            boolean nuevoEstado = !usuario.isEnabled();
            usuario.setEnabled(nuevoEstado);
            userRepository.save(usuario);

            String accion = nuevoEstado ? "activado" : "desactivado";
            redirectAttributes.addFlashAttribute("mensaje",
                    "Usuario " + usuario.getUsername() + " " + accion + " exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al cambiar el estado: " + e.getMessage());
        }

        return "redirect:/usuarios";
    }

}