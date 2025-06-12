package com.tcna.primeraweb.services.impl;

import com.tcna.primeraweb.models.Pregunta;
import com.tcna.primeraweb.repositories.PreguntaRepository;
import com.tcna.primeraweb.services.PreguntaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PreguntaServiceImpl implements PreguntaService {

    private final PreguntaRepository preguntaRepository;

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
        List<Pregunta> preguntas = preguntaRepository.findPreguntaAleatoriaPorCategoriaExcluyendo(categoriaId, preguntasRespondidas);
        if (preguntas.isEmpty()) {
            return null; // No hay preguntas disponibles
        }
        return preguntas.get(0); // Devuelve la primera pregunta aleatoria
    }

}