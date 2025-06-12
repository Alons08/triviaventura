package com.tcna.primeraweb.repositories;

import com.tcna.primeraweb.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {


    //INGLES
    //Aqui puedo poner más metodos a parte del CRUD [1]

    // Método para obtener todos los juegos de un usuario específico
    List<Juego> findByUsuarioId(Long usuarioId);

    // Otros métodos existentes
    List<Juego> findTop10ByOrderByPuntajeDesc();

    @Query("SELECT j FROM Juego j WHERE j.fechaInicio = " +
            "(SELECT MAX(j2.fechaInicio) FROM Juego j2 WHERE j2.usuario = j.usuario)")
    List<Juego> findUltimosJuegosPorUsuario();

    @Query("SELECT j FROM Juego j WHERE j.usuario.id = :usuarioId AND j.categoria.id = :categoriaId ORDER BY j.fechaInicio DESC")
    List<Juego> findUltimosJuegosPorUsuarioYCategoria(Long usuarioId, Long categoriaId);

    @Query("SELECT j.usuario.id, SUM(j.puntaje) FROM Juego j GROUP BY j.usuario.id ORDER BY SUM(j.puntaje) DESC")
    List<Object[]> findRankingUsuariosPorPuntajeTotal();

    @Query("SELECT j.usuario.username, SUM(j.puntaje) FROM Juego j WHERE j.fechaInicio = " +
            "(SELECT MAX(j2.fechaInicio) FROM Juego j2 WHERE j2.usuario = j.usuario AND j2.categoria = j.categoria) " +
            "GROUP BY j.usuario.username ORDER BY SUM(j.puntaje) DESC")
    List<Object[]> findRankingUsuariosPorUltimosJuegos();

    @Query("SELECT SUM(j.puntaje) FROM Juego j WHERE j.usuario.username = :username AND j.fechaInicio = " +
            "(SELECT MAX(j2.fechaInicio) FROM Juego j2 WHERE j2.usuario.username = :username AND j2.categoria = j.categoria)")
    Optional<Integer> calculateTotalScoreByUsername(String username);

}