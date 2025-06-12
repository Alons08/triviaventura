package com.tcna.primeraweb.repositories;

import com.tcna.primeraweb.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    //INGLES
    //Aqui puedo poner más metodos a parte del CRUD [1]

}
