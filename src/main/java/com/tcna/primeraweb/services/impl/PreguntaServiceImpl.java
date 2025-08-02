package com.tcna.primeraweb.services.impl;

import com.tcna.primeraweb.models.Pregunta;
import com.tcna.primeraweb.repositories.PreguntaRepository;
import com.tcna.primeraweb.services.PreguntaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PreguntaServiceImpl implements PreguntaService {

    private final PreguntaRepository preguntaRepository;

    @Override
    public Page<Pregunta> listarTodosPaginado(Pageable pageable) {
        return preguntaRepository.findAll(pageable);
    }

    @Override
    public List<Pregunta> listarTodos() {
        return preguntaRepository.findAll();
    }

    @Override
    public Pregunta obtenerPorId(Long id) {
        return preguntaRepository.findById(id).orElse(null);
    }

    @Override
    public Pregunta crear(Pregunta objeto) {
        return preguntaRepository.save(objeto);
    }

    @Override
    public Pregunta actualizar(Long id, Pregunta objeto) {
        if (preguntaRepository.existsById(id)){
            return preguntaRepository.save(objeto);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        if (preguntaRepository.existsById(id)){
            preguntaRepository.deleteById(id);
        }
    }

    //nuevoo
    @Override
    public Pregunta obtenerPreguntaAleatoriaPorCategoria(Long categoriaId) {
        List<Pregunta> preguntas = preguntaRepository.findByCategoriaId(categoriaId);
        if(preguntas.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return preguntas.get(random.nextInt(preguntas.size()));
    }

    @Override
    public Pregunta obtenerPreguntaAleatoriaPorCategoriaExcluyendo(Long categoriaId, List<Long> preguntasRespondidas) {
        // Asegurar que la lista no sea null
        if (preguntasRespondidas == null) {
            preguntasRespondidas = new java.util.ArrayList<>();
        }
        
        List<Pregunta> preguntas = preguntaRepository.findPreguntaAleatoriaPorCategoriaExcluyendo(categoriaId, preguntasRespondidas);
        
        if (preguntas.isEmpty()) {
            return null; // No hay preguntas disponibles
        }
        
        // Mezclar la lista para mayor aleatoriedad si hay múltiples resultados
        if (preguntas.size() > 1) {
            java.util.Collections.shuffle(preguntas);
        }
        
        return preguntas.get(0); // Devuelve la primera pregunta después de mezclar
    }

    @Override
    public int contarPreguntasPorCategoria(Long categoriaId) {
        return preguntaRepository.countByCategoriaId(categoriaId);
    }

    @Override
    public List<Pregunta> obtenerPreguntasAleatoriasPorCategoria(Long categoriaId, int cantidad) {
        // Obtener TODAS las preguntas de la categoría
        List<Pregunta> todasLasPreguntas = preguntaRepository.findByCategoriaId(categoriaId);
        
        if (todasLasPreguntas.size() < cantidad) {
            return new java.util.ArrayList<>();
        }
        
        // Crear lista de índices únicos
        java.util.List<Integer> indices = new java.util.ArrayList<>();
        for (int i = 0; i < todasLasPreguntas.size(); i++) {
            indices.add(i);
        }
        
        // Mezclar los índices usando Java Collections
        java.util.Collections.shuffle(indices, new java.util.Random(System.currentTimeMillis()));
        
        // Seleccionar las primeras 'cantidad' preguntas usando los índices mezclados
        List<Pregunta> resultado = new java.util.ArrayList<>();
        java.util.Set<Long> idsVerificacion = new java.util.HashSet<>();
        
        for (int i = 0; i < cantidad && i < indices.size(); i++) {
            Pregunta pregunta = todasLasPreguntas.get(indices.get(i));
            if (idsVerificacion.add(pregunta.getId())) {
                resultado.add(pregunta);
            }
        }
        
        return resultado;
    }

}