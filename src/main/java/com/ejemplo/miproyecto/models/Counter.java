package com.ejemplo.miproyecto.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "contadores")
public class Counter {
    @Id
    private String id;
    private String name;
    private int value;
}
