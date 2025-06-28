package com.tcna.primeraweb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "no debe estar vacío")
    @Size(min = 3, max = 50, message = "debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "no debe estar vacío")
    @Size(min = 5, max = 200, message = "debe tener minimo 5 caracteres")
    private String password;

    private boolean enabled;

    //HashSet porque puede tener un conjunto de roles (varios roles)
    //Set porque no pueden haber elementos repetidos, o sea los roles son únicos
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private LocalDateTime fecharegistro = LocalDateTime.now();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Juego> juegos;


    public User() {
    }

    // Constructor adicional
    public User(Long id) {
        this.id = id;
    }

}


