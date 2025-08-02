package com.tcna.primeraweb.services;

import com.tcna.primeraweb.models.Juego;

import java.util.List;

public interface JuegoService extends ICRUD<Juego, Long>{

    //Ya tengo todo el CRUD, pero podría poner otros metodos [2]
    // Nuevo método: Actualizar puntaje por categoría
    void actualizarPuntajePorCategoria(Long usuarioId, Long categoriaId, int nuevoPuntaje);

    List<Object[]> obtenerRankingUsuariosPorUltimosJuegos();

    int calcularPuntajeTotalPorUsuario(String username);

}