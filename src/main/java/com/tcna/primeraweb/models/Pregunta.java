package com.tcna.primeraweb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "preguntas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"categoria", "detalleJuegos"}) // Excluir relaciones
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @NotBlank
    @Size(min = 8, max= 150)
    @Column(columnDefinition = "TEXT")
    private String textoPregunta;

    @NotBlank
    @Size(min = 1, max= 30) //poner mas numero aqui mas adelante
    private String opcionA;

    @NotBlank
    @Size(min = 1, max= 30)
    private String opcionB;

    @NotBlank
    @Size(min = 1, max= 30)
    private String opcionC;

    @Pattern(regexp = "[ABC]")
    private String respuestaCorrecta;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleJuego> detalleJuegos;

}
