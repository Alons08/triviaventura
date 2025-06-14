package com.tcna.primeraweb.repositories;

import com.tcna.primeraweb.models.DetalleJuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleJuegoRepository extends JpaRepository<DetalleJuego, Long> {

    //INGLES
    //Aqui puedo poner más metodos a parte del CRUD [1]

    @Query("SELECT d.pregunta.id FROM DetalleJuego d WHERE d.juego.id = :juegoId")
    List<Long> findPreguntasRespondidasPorJuego(@Param("juegoId") Long juegoId);

    List<DetalleJuego> findByJuegoId(Long juegoId);

    @Query("SELECT d FROM DetalleJuego d WHERE d.juego.id = :juegoId AND d.pregunta.id = :preguntaId")
    Optional<DetalleJuego> findByJuegoIdAndPreguntaId(@Param("juegoId") Long juegoId, @Param("preguntaId") Long preguntaId);

}
