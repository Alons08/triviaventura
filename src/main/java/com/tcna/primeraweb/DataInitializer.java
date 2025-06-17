package com.tcna.primeraweb;

import com.tcna.primeraweb.models.Role;
import com.tcna.primeraweb.models.User;
import com.tcna.primeraweb.repositories.RoleRepository;
import com.tcna.primeraweb.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            //AUTOMATICAMENTEEEEEEEE

            // Crear el rol ADMIN si no existe
            Role adminRole = roleRepository.findByNombre("ADMIN").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setNombre("ADMIN");
                return roleRepository.save(newRole);
            });

            // Crear el rol USER si no existe
            Role userRole = roleRepository.findByNombre("USER").orElseGet(() -> {
                Role newRole = new Role();
                newRole.setNombre("USER");
                return roleRepository.save(newRole);
            });


            // Crear usuario ADMIN por defecto si no existe
            if (!userRepository.existsByUsername("alonso")) { //"admin"
                User admin = new User();
                admin.setUsername("alonso"); //"admin"
                admin.setPassword(encoder.encode("password"));
                admin.setEnabled(true);
                admin.setRoles(Collections.singleton(adminRole)); // asignar rol ADMIN
                userRepository.save(admin);
            }     /* System.out.println("SI se ha creado un usuario ADMIN");
            } else {
                System.out.println("NO se ha creado un usuario ADMIN porque ya existe");
            }*/
        };
    }

}
