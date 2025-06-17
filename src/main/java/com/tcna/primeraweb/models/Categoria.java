package com.tcna.primeraweb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "preguntas") // Excluir la lista de preguntas
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "no debe estar vacío")
    @Size(min = 3, max = 50, message = "debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "no debe estar vacía")
    @Size(min = 10, max = 100, message = "debe tener entre 10 y 50 caracteres")
    private String descripcion;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY) //estaba EAGER
    private List<Pregunta> preguntas = new ArrayList<>();

    // Constructor adicional
    public Categoria(Long id) {
        this.id = id;
    }

}
