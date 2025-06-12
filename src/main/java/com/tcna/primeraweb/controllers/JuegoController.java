package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.*;
import com.tcna.primeraweb.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/jugar")
@RequiredArgsConstructor
public class JuegoController {

    private final CategoriaService categoriaService;
    private final PreguntaService preguntaService;
    private final JuegoService juegoService;
    private final DetalleJuegoService detalleJuegoService;

    @GetMapping
    public String seleccionarCategoria(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodos());
        return "seleccionar-categoria";
    }

    @PostMapping("/iniciar")
    public String iniciarJuego(@RequestParam Long categoriaId,
                               @AuthenticationPrincipal MyUserDetails userDetails) {
        // Crear un nuevo juego
        Juego juego = new Juego();
        juego.setUsuario(userDetails.getUser());
        juego.setCategoria(new Categoria(categoriaId)); // Asociar la categoría al juego
        juego.setFechaInicio(LocalDateTime.now());
        juego = juegoService.crear(juego);

        return "redirect:/jugar/pregunta?juegoId=" + juego.getId() + "&categoriaId=" + categoriaId;
    }


    @GetMapping("/pregunta")
    public String mostrarPregunta(@RequestParam Long juegoId,
                                @RequestParam Long categoriaId,
                                Model model) {
        Juego juego = juegoService.obtenerPorId(juegoId);

        // Obtener preguntas ya respondidas
        List<Long> preguntasRespondidas = detalleJuegoService.obtenerPreguntasRespondidasPorJuego(juegoId);

        // Obtener una pregunta aleatoria que no haya sido respondida
        Pregunta pregunta = preguntaService.obtenerPreguntaAleatoriaPorCategoriaExcluyendo(categoriaId, preguntasRespondidas);

        if (pregunta == null) {
            // Si no hay más preguntas, finalizar el juego
            return "redirect:/jugar/finalizar/" + juegoId;
        }

        // Calcular el número de la pregunta actual y el total de preguntas
        int preguntaActual = preguntasRespondidas.size() + 1;
        int totalPreguntas = preguntaService.contarPreguntasPorCategoria(categoriaId);

        model.addAttribute("juego", juego);
        model.addAttribute("pregunta", pregunta);
        model.addAttribute("juegoId", juegoId);
        model.addAttribute("preguntaActual", preguntaActual);
        model.addAttribute("totalPreguntas", totalPreguntas);

        return "pregunta";
    }


    @PostMapping("/responder")
    public String procesarRespuesta(@RequestParam Long juegoId,
                                    @RequestParam Long preguntaId,
                                    @RequestParam String respuestaUsuario) {
        // Procesar respuesta y guardar detalle
        Juego juego = juegoService.obtenerPorId(juegoId);
        Pregunta pregunta = preguntaService.obtenerPorId(preguntaId);

        DetalleJuego detalle = new DetalleJuego();
        detalle.setJuego(juego);
        detalle.setPregunta(pregunta);
        detalle.setRespuestaUsuario(respuestaUsuario);
        detalle.setEsCorrecta(respuestaUsuario.equals(pregunta.getRespuestaCorrecta()));

        detalleJuegoService.crear(detalle);

        // Actualizar puntaje si la respuesta es correcta
        if(detalle.isEsCorrecta()) {
            juego.setPuntaje(juego.getPuntaje() + 10); // +10 puntos por respuesta correcta
            juegoService.actualizar(juegoId, juego);
        }

        return "redirect:/jugar/pregunta?juegoId=" + juegoId + "&categoriaId=" + pregunta.getCategoria().getId();
    }

    @GetMapping("/finalizar/{juegoId}")
    public String finalizarJuego(@PathVariable Long juegoId, Model model) {
        Juego juego = juegoService.obtenerPorId(juegoId);
        juego.setFechaFin(LocalDateTime.now());
        juegoService.actualizar(juegoId, juego);

        // Actualizar el puntaje acumulado por categoría
        juegoService.actualizarPuntajePorCategoria(juego.getUsuario().getId(), juego.getCategoria().getId(), juego.getPuntaje());

        model.addAttribute("puntaje", juego.getPuntaje());
        return "resultado";
    }

}