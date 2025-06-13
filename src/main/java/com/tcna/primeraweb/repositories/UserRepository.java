package com.tcna.primeraweb.repositories;

import com.tcna.primeraweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Query personalizado para buscar un usuario por su username
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);

    // Método para verificar si un username ya existe
    boolean existsByUsername(String username);

}

