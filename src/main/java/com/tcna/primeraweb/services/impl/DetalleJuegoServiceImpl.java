package com.tcna.primeraweb.services.impl;

import com.tcna.primeraweb.models.DetalleJuego;
import com.tcna.primeraweb.repositories.DetalleJuegoRepository;
import com.tcna.primeraweb.services.DetalleJuegoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetalleJuegoServiceImpl implements DetalleJuegoService {

    private final DetalleJuegoRepository detalleJuegoRepository;

    @Override
    public List<DetalleJuego> listarTodos() {
        return detalleJuegoRepository.findAll();
    }

    @Override
    public DetalleJuego obtenerPorId(Long id) {
        return detalleJuegoRepository.findById(id).orElse(null);
    }

    @Override
    public DetalleJuego crear(DetalleJuego objeto) {
        return detalleJuegoRepository.save(objeto);
    }

    @Override
    public DetalleJuego actualizar(Long id, DetalleJuego objeto) {
        if (detalleJuegoRepository.existsById(id)){
            return detalleJuegoRepository.save(objeto);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        if (detalleJuegoRepository.existsById(id)){
            detalleJuegoRepository.deleteById(id);
        }
    }

    //nuevoooo
    @Override
    public List<Long> obtenerPreguntasRespondidasPorJuego(Long juegoId) {
        return detalleJuegoRepository.findPreguntasRespondidasPorJuego(juegoId);
    }

}
