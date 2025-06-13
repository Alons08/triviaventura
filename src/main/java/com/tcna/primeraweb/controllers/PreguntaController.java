package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.Pregunta;
import com.tcna.primeraweb.services.CategoriaService;
import com.tcna.primeraweb.services.PreguntaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/preguntas")
@RequiredArgsConstructor
public class PreguntaController {

    private final PreguntaService preguntaService;
    private final CategoriaService categoriaService;

    @GetMapping
    public String listarPreguntas(Model model) {
        model.addAttribute("preguntas", preguntaService.listarTodos());
        return "pregunta/listado"; //archivo html
    }

    @GetMapping("/nueva")
    public String mostrarFormularioDeNuevaPregunta(Model model) {
        model.addAttribute("pregunta", new Pregunta());
        model.addAttribute("categorias",categoriaService.listarTodos());
        return "pregunta/formulario"; //archivo html
    }

    @PostMapping("/guardar")
    public String guardarPregunta(@Valid @ModelAttribute Pregunta pregunta,
                                BindingResult bindingResult,
                                Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("categorias", categoriaService.listarTodos()); // ← Esto es importante
            return "pregunta/formulario";
        }
        preguntaService.crear(pregunta);
        return "redirect:/preguntas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarPregunta(@PathVariable Long id, Model model) {
        model.addAttribute("pregunta", preguntaService.obtenerPorId(id));
        model.addAttribute("categorias", categoriaService.listarTodos());
        return "pregunta/formulario"; //archivo html
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPregunta(@PathVariable Long id) {
        preguntaService.eliminar(id);
        return ("redirect:/preguntas"); //endpoint
    }

}