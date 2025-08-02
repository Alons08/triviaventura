package com.tcna.primeraweb.repositories;

import com.tcna.primeraweb.models.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    // El método findAll(Pageable) ya viene incluido en JpaRepository

    //INGLES
    //Aqui puedo poner más metodos a parte del CRUD [1]
    List<Pregunta> findByCategoriaId(Long categoriaId);

    @Query(value = "SELECT * FROM preguntas WHERE id_categoria = :categoriaId AND (:#{#preguntasRespondidas.size()} = 0 OR id NOT IN (:preguntasRespondidas)) ORDER BY RANDOM()", nativeQuery = true)
    List<Pregunta> findPreguntaAleatoriaPorCategoriaExcluyendo(@Param("categoriaId") Long categoriaId, @Param("preguntasRespondidas") List<Long> preguntasRespondidas);

    int countByCategoriaId(Long categoriaId);

}
