package com.tcna.primeraweb.repositories;

import com.tcna.primeraweb.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    //INGLES
    //Aqui puedo poner más metodos a parte del CRUD [1]
    Optional<Role> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

}