package com.tcna.primeraweb.repositories;

import com.tcna.primeraweb.models.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {


    //INGLES
    //Aqui puedo poner más metodos a parte del CRUD [1]

    @Query("SELECT j FROM Juego j WHERE j.usuario.id = :usuarioId AND j.categoria.id = :categoriaId ORDER BY j.fechaInicio DESC")
    List<Juego> findUltimosJuegosPorUsuarioYCategoria(Long usuarioId, Long categoriaId);

    @Query("SELECT j FROM Juego j WHERE j.usuario.username = :username ORDER BY j.fechaInicio DESC")
    List<Juego> findByUsuarioUsernameOrderByFechaInicioDesc(@Param("username") String username);

    @Query("SELECT u.username, SUM(CASE WHEN dj.esCorrecta = true THEN 10 ELSE 0 END) as puntajeTotal " +
            "FROM DetalleJuego dj " +
            "JOIN Juego j ON dj.juego.id = j.id " +
            "JOIN User u ON j.usuario.id = u.id " +  // Cambiado de u.user_id a u.id
            "WHERE dj.id IN (" +
            "    SELECT MAX(dj2.id) FROM DetalleJuego dj2 " +
            "    JOIN Juego j2 ON dj2.juego.id = j2.id " +
            "    WHERE j2.usuario.id = u.id " +  // Cambiado de u.user_id a u.id
            "    GROUP BY dj2.pregunta.id" +
            ") " +
            "GROUP BY u.username " +
            "ORDER BY puntajeTotal DESC")
    List<Object[]> findRankingUsuariosPorUltimosJuegos();

}