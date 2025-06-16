package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/listausuarios")
@RequiredArgsConstructor
public class ListaUsuarioController {

    private final UserRepository userRepository;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", userRepository.findAll());
        return "usuario/listado"; //archivo html
    }

}