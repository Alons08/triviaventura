package com.tcna.primeraweb.services.impl;

import com.tcna.primeraweb.models.Categoria;
import com.tcna.primeraweb.models.Juego;
import com.tcna.primeraweb.models.User;
import com.tcna.primeraweb.repositories.JuegoRepository;
import com.tcna.primeraweb.services.JuegoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JuegoServiceImpl implements JuegoService {

    private final JuegoRepository juegoRepository;

    @Override
    public List<Juego> listarTodos() {
        return juegoRepository.findAll();
    }

    @Override
    public Juego obtenerPorId(Long id) {
        return juegoRepository.findById(id).orElse(null);
    }

    @Override
    public Juego crear(Juego objeto) {
        return juegoRepository.save(objeto);
    }

    @Override
    public Juego actualizar(Long id, Juego objeto) {
        if (juegoRepository.existsById(id)){
            return juegoRepository.save(objeto);
        }
        return null;
    }

    @Override
    public void eliminar(Long id) {
        if (juegoRepository.existsById(id)){
            juegoRepository.deleteById(id);
        }
    }

    //nuevooo
    @Override
    public List<Juego> obtenerTop10Juegos() {
        return juegoRepository.findAll().stream()
                .collect(Collectors.groupingBy(juego -> juego.getUsuario().getUsername(),
                        Collectors.maxBy(Comparator.comparingInt(Juego::getPuntaje))))
                .values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(Comparator.comparingInt(Juego::getPuntaje).reversed())
                .limit(10)
                .toList();
    }

    @Override
    public List<Juego> obtenerUltimosJuegosPorUsuario() {
        return juegoRepository.findUltimosJuegosPorUsuario().stream()
                .sorted(Comparator.comparingInt(Juego::getPuntaje).reversed())
                .limit(10)
                .toList();
    }

    @Override
    public void actualizarPuntajePorCategoria(Long usuarioId, Long categoriaId, int nuevoPuntaje) {
        List<Juego> juegos = juegoRepository.findUltimosJuegosPorUsuarioYCategoria(usuarioId, categoriaId);

        if (!juegos.isEmpty()) {
            // Tomar el último juego (primer elemento de la lista ordenada)
            Juego ultimoJuego = juegos.get(0);
            ultimoJuego.setPuntaje(nuevoPuntaje);
            juegoRepository.save(ultimoJuego);
        } else {
            // Crear un nuevo registro si no existe
            Juego nuevoJuego = new Juego();
            nuevoJuego.setUsuario(new User(usuarioId));
            nuevoJuego.setCategoria(new Categoria(categoriaId));
            nuevoJuego.setPuntaje(nuevoPuntaje);
            nuevoJuego.setFechaInicio(LocalDateTime.now());
            juegoRepository.save(nuevoJuego);
        }
    }

    @Override
    public int calcularPuntajeTotal(Long usuarioId) {
        return juegoRepository.findByUsuarioId(usuarioId)
                .stream()
                .mapToInt(Juego::getPuntaje)
                .sum();
    }

    @Override
    public List<Object[]> obtenerRankingUsuariosPorPuntajeTotal() {
        return juegoRepository.findRankingUsuariosPorPuntajeTotal();
    }

    @Override
    public List<Object[]> obtenerRankingUsuariosPorUltimosJuegos() {
        return juegoRepository.findRankingUsuariosPorUltimosJuegos();
    }

    @Override
    public int calcularPuntajeTotalPorUsuario(String username) {
        return juegoRepository.calculateTotalScoreByUsername(username).orElse(0); // Devuelve 0 si no hay puntaje
    }

}
