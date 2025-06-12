package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.services.JuegoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RankingController {

    private final JuegoService juegoService;

    @GetMapping("/ranking")
    public String mostrarRanking(Model model, Principal principal) {
        List<Object[]> ranking = juegoService.obtenerRankingUsuariosPorUltimosJuegos();
        model.addAttribute("ranking", ranking);

        if (principal != null) {
            int puntajeActual = juegoService.calcularPuntajeTotalPorUsuario(principal.getName());
            model.addAttribute("puntajeActual", puntajeActual);
        } else {
            model.addAttribute("puntajeActual", 0);
        }

        return "ranking";
    }

}