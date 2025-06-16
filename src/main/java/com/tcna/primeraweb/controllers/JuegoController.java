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
        return "seleccionar-categoria"; //archivo html
    }

    @PostMapping("/iniciar")
    public String iniciarJuego(@RequestParam Long categoriaId,
                               @AuthenticationPrincipal MyUserDetails userDetails,
                               Model model) {
        try {
            // Verificar que la categoría tenga al menos 7 preguntas
            int totalPreguntas = preguntaService.contarPreguntasPorCategoria(categoriaId);
            if(totalPreguntas < 7) {
                model.addAttribute("error", "La categoría seleccionada no tiene suficientes preguntas (mínimo 7)");
                return "redirect:/jugar";
            }
            // Crear un nuevo juego
            Juego juego = new Juego();
            juego.setUsuario(userDetails.getUser());
            juego.setCategoria(new Categoria(categoriaId));
            juego.setFechaInicio(LocalDateTime.now());
            juego = juegoService.crear(juego);

            // Obtener 7 preguntas aleatorias para este juego
            List<Pregunta> preguntasJuego = preguntaService.obtenerPreguntasAleatoriasPorCategoria(categoriaId, 7);
            // Crear los detalles de juego con las preguntas seleccionadas
            for (Pregunta pregunta : preguntasJuego) {
                DetalleJuego detalle = new DetalleJuego();
                detalle.setJuego(juego);
                detalle.setPregunta(pregunta);
                detalleJuegoService.crear(detalle);
            }
            return "redirect:/jugar/pregunta?juegoId=" + juego.getId() + "&preguntaNum=1";
        } catch (Exception e) {
            model.addAttribute("error", "Error al iniciar el juego: " + e.getMessage());
            return "redirect:/jugar";
        }
    }

    @GetMapping("/pregunta")
    public String mostrarPregunta(@RequestParam Long juegoId,
                                  @RequestParam int preguntaNum,
                                  Model model) {
        try {
            Juego juego = juegoService.obtenerPorId(juegoId);
            if(juego == null) {
                return "redirect:/jugar?error=Juego no encontrado";
            }

            List<DetalleJuego> detalles = detalleJuegoService.obtenerDetallesPorJuego(juegoId);
            if(detalles.isEmpty()) {
                return "redirect:/jugar?error=No hay preguntas para este juego";
            }

            if(preguntaNum > detalles.size()) {
                return "redirect:/jugar/finalizar/" + juegoId;
            }

            DetalleJuego detalleActual = detalles.get(preguntaNum - 1);
            Pregunta pregunta = detalleActual.getPregunta();
            model.addAttribute("juego", juego);
            model.addAttribute("pregunta", pregunta);
            model.addAttribute("juegoId", juegoId);
            model.addAttribute("preguntaActual", preguntaNum);
            model.addAttribute("totalPreguntas", detalles.size());
            return "pregunta";
        } catch (Exception e) {
            return "redirect:/jugar?error=Ocurrió un error inesperado";
        }
    }

    @PostMapping("/responder")
    public String procesarRespuesta(@RequestParam Long juegoId,
                                    @RequestParam Long preguntaId,
                                    @RequestParam(required = false) String respuestaUsuario,
                                    @RequestParam int preguntaActual) {
        try {
            Juego juego = juegoService.obtenerPorId(juegoId);
            Pregunta pregunta = preguntaService.obtenerPorId(preguntaId);

            // Si no se proporcionó respuesta (tiempo agotado), considerar como incorrecta
            if(respuestaUsuario == null) {
                respuestaUsuario = "";
            }

            DetalleJuego detalle = detalleJuegoService.obtenerDetallePorJuegoYPregunta(juegoId, preguntaId);
            detalle.setRespuestaUsuario(respuestaUsuario);
            detalle.setEsCorrecta(respuestaUsuario.equals(pregunta.getRespuestaCorrecta()));
            detalleJuegoService.actualizar(detalle.getId(), detalle);
            if(detalle.isEsCorrecta()) {
                juego.setPuntaje(juego.getPuntaje() + 10);
                juegoService.actualizar(juegoId, juego);
            }

            // Verificar si es la última pregunta
            int totalPreguntas = detalleJuegoService.obtenerDetallesPorJuego(juegoId).size();
            if(preguntaActual >= totalPreguntas) {
                return "redirect:/jugar/finalizar/" + juegoId;
            } else {
                return "redirect:/jugar/pregunta?juegoId=" + juegoId + "&preguntaNum=" + (preguntaActual + 1);
            }
        } catch (Exception e) {
            return "redirect:/jugar?error=Error al procesar respuesta";
        }
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