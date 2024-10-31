package com.ejemplo.miproyecto.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "productos")
public class Product {
    @Id
    private String id;
    private int productoId;
    private String nombre;
    private String descripcion;
    private int cantidadesDisponibles;
    private int categoriaId;
    private int usuarioId;
}
