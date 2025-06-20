package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.Categoria;
import com.tcna.primeraweb.services.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @GetMapping
    public String listarCategorias(Model model){
        model.addAttribute("categorias", service.listarTodos());
        return "categoria/listado"; //archivo html
    }

    @GetMapping("/nueva")
    public String mostrarFormularioDeNuevaCategoria(Model model){
        model.addAttribute("categoria", new Categoria());
        return "categoria/formulario"; //archivo html
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@Valid @ModelAttribute Categoria categoria,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "categoria/formulario"; //si hay error seguimos en el archivo html
        }
        service.crear(categoria);
        return "redirect:/categorias"; //endpoint
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarCategoria(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", service.obtenerPorId(id));
        return "categoria/formulario"; //archivo html
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id){
        service.eliminar(id);
        return ("redirect:/categorias"); //endpoint
    }

}