package com.ejemplo.miproyecto.repositories;

import com.ejemplo.miproyecto.models.Ingreso;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IngresoRepository extends MongoRepository<Ingreso, String> {
}
