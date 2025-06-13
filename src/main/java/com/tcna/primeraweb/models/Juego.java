package com.tcna.primeraweb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "juegos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @NotNull
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_categoria") // Relación con la categoría
    @NotNull
    private Categoria categoria;

    private LocalDateTime fechaInicio = LocalDateTime.now();

    private LocalDateTime fechaFin;

    private Integer puntaje = 0;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetalleJuego> detalleJuegos;
    
}