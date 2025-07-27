package com.tcna.primeraweb.services;

import com.tcna.primeraweb.models.Pregunta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PreguntaService extends ICRUD<Pregunta, Long>{

    // Método para paginación
    Page<Pregunta> listarTodosPaginado(Pageable pageable);

    //Ya tengo todo el CRUD, pero podría poner otros metodos [2]
    Pregunta obtenerPreguntaAleatoriaPorCategoria(Long categoriaId);

    Pregunta obtenerPreguntaAleatoriaPorCategoriaExcluyendo(Long categoriaId, List<Long> preguntasRespondidas); // Nuevo método
    
    int contarPreguntasPorCategoria(Long categoriaId);

    List<Pregunta> obtenerPreguntasAleatoriasPorCategoria(Long categoriaId, int cantidad);
}
