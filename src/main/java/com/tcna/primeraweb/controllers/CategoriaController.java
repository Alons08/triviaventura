package com.tcna.primeraweb.controllers;

import com.tcna.primeraweb.models.Categoria;
import com.tcna.primeraweb.services.CategoriaService;
import com.tcna.primeraweb.services.CloudinaryService;
import com.tcna.primeraweb.services.UploadResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;
    private final CloudinaryService cloudinaryService;

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
                                   @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "categoria/formulario"; //si hay error seguimos en el archivo html
        }
        
        try {
            // Si se seleccionó una nueva imagen
            if (imagenFile != null && !imagenFile.isEmpty()) {
                // Validar tipo de archivo
                String contentType = imagenFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute("error", "El archivo debe ser una imagen");
                    return "categoria/formulario";
                }
                
                // Si es una edición y ya tiene imagen, eliminar la anterior
                if (categoria.getId() != null) {
                    Categoria categoriaExistente = service.obtenerPorId(categoria.getId());
                    if (categoriaExistente != null && categoriaExistente.getPublicId() != null) {
                        cloudinaryService.deleteFile(categoriaExistente.getPublicId());
                    }
                }
                
                // Subir nueva imagen
                UploadResult uploadResult = cloudinaryService.uploadFile(imagenFile);
                categoria.setImagenUrl(uploadResult.getUrl());
                categoria.setPublicId(uploadResult.getPublicId());
            } else if (categoria.getId() != null) {
                // Si es edición sin nueva imagen, mantener la URL y public_id existentes
                Categoria categoriaExistente = service.obtenerPorId(categoria.getId());
                if (categoriaExistente != null) {
                    categoria.setImagenUrl(categoriaExistente.getImagenUrl());
                    categoria.setPublicId(categoriaExistente.getPublicId());
                }
            }
            
            if (categoria.getId() != null) {
                service.actualizar(categoria.getId(), categoria);
            } else {
                service.crear(categoria);
            }
            
            return "redirect:/categorias"; //endpoint
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar la imagen: " + e.getMessage());
            return "categoria/formulario";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioDeEditarCategoria(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", service.obtenerPorId(id));
        return "categoria/formulario"; //archivo html
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCategoria(@PathVariable Long id){
        // Obtener la categoría antes de eliminarla para borrar la imagen
        Categoria categoria = service.obtenerPorId(id);
        if (categoria != null && categoria.getPublicId() != null) {
            try {
                cloudinaryService.deleteFile(categoria.getPublicId());
            } catch (Exception e) {
                // Log del error pero continuar con la eliminación de la categoría
                System.err.println("Error al eliminar imagen: " + e.getMessage());
            }
        }
        
        service.eliminar(id);
        return ("redirect:/categorias"); //endpoint
    }

}