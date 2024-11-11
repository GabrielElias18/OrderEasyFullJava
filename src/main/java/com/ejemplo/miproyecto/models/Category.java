package com.ejemplo.miproyecto.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "categorias")
public class Category {
    @Id
    private String id;
    private int categoriaId;
    private String nombre;
    private String descripcion;
}
