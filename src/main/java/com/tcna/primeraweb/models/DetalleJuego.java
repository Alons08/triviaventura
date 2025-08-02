package com.tcna.primeraweb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_juegos", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"id_juego", "id_pregunta"})
       })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleJuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_juego")
    private Juego juego;

    @ManyToOne
    @JoinColumn(name = "id_pregunta")
    @NotNull
    private Pregunta pregunta;

    @Pattern(regexp = "[ABC]")
    private String respuestaUsuario;

    private boolean esCorrecta;

}
