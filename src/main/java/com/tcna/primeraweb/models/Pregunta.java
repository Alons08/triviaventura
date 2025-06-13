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

    @NotBlank(message = "no debe estar vacío")
    @Size(min = 8, max = 150, message = "debe tener entre 8 y 150 caracteres")
    @Column(columnDefinition = "TEXT")
    private String textoPregunta;

    @NotBlank(message = "no debe estar vacío")
    @Size(min = 1, max = 40, message = "debe tener entre 1 y 40 caracteres")
    private String opcionA;

    @NotBlank(message = "no debe estar vacío")
    @Size(min = 1, max = 40, message = "debe tener entre 1 y 40 caracteres")
    private String opcionB;

    @NotBlank(message = "no debe estar vacío")
    @Size(min = 1, max = 40, message = "debe tener entre 1 y 40 caracteres")
    private String opcionC;

    @Pattern(regexp = "[ABC]")
    private String respuestaCorrecta;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleJuego> detalleJuegos;

}
