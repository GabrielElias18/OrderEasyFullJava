package com.ejemplo.miproyecto.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "usuarios")
public class User {
    @Id
    private String id;
    private int usuarioId;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
}
