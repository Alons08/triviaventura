package com.tcna.primeraweb.services;

import com.tcna.primeraweb.models.DetalleJuego;

import java.util.List;

public interface DetalleJuegoService extends ICRUD<DetalleJuego, Long>{

    //Ya tengo todo el CRUD, pero podría poner otros metodos [2]
    List<Long> obtenerPreguntasRespondidasPorJuego(Long juegoId); // Nuevo método

    List<DetalleJuego> obtenerDetallesPorJuego(Long juegoId);
    DetalleJuego obtenerDetallePorJuegoYPregunta(Long juegoId, Long preguntaId);

}
