package com.ejemplo.miproyecto.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "ingresos")
public class Ingreso {
    @Id
    private String id; // ObjectId de MongoDB
    private String productoNombre; // Campo para nombre del producto
    private int cantidad;
    private double total;
    private LocalDateTime fecha;
}
