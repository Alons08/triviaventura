package com.tcna.primeraweb.services.impl;

import com.tcna.primeraweb.models.Categoria;
import com.tcna.primeraweb.repositories.CategoriaRepository;
import com.tcna.primeraweb.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listarTodos() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    public Categoria crear(Categoria objeto) {
        return categoriaRepository.save(objeto);
    }

    @Override
    public Categoria actualizar(Long id, Categoria objeto) {
        if (categoriaRepository.existsById(id)){
            return categoriaRepository.save(objeto);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        if (categoriaRepository.existsById(id)){
            categoriaRepository.deleteById(id);
        }
    }

}