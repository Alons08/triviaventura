package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.Pregunta;
import com.tcna.primeraweb.services.CategoriaService;
import com.tcna.primeraweb.services.PreguntaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String listarPreguntas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
            
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Pregunta> preguntasPage = preguntaService.listarTodosPaginado(pageable);
        
        model.addAttribute("preguntasPage", preguntasPage);
        model.addAttribute("preguntas", preguntasPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", preguntasPage.getTotalPages());
        model.addAttribute("totalElements", preguntasPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
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
            return "pregunta/formulario"; //si hay error seguimos en el archivo html
        }
        preguntaService.crear(pregunta);
        return "redirect:/preguntas"; //endpoint
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