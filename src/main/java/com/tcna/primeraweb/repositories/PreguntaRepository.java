package com.tcna.primeraweb.repositories;

import com.tcna.primeraweb.models.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    //INGLES
    //Aqui puedo poner más metodos a parte del CRUD [1]
    List<Pregunta> findByCategoriaId(Long categoriaId);

    @Query(value = "SELECT p FROM Pregunta p WHERE p.categoria.id = :categoriaId AND p.id NOT IN :preguntasRespondidas ORDER BY RAND()")
    List<Pregunta> findPreguntaAleatoriaPorCategoriaExcluyendo(@Param("categoriaId") Long categoriaId, @Param("preguntasRespondidas") List<Long> preguntasRespondidas);

    int countByCategoriaId(Long categoriaId);

    @Query(value = "SELECT * FROM preguntas WHERE id_categoria = :categoriaId ORDER BY RAND() LIMIT :cantidad", nativeQuery = true)
    List<Pregunta> findRandomByCategoria(@Param("categoriaId") Long categoriaId, @Param("cantidad") int cantidad);

}
