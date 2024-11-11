package com.ejemplo.miproyecto.repositories;

import com.ejemplo.miproyecto.models.Egreso;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EgresoRepository extends MongoRepository<Egreso, String> {
}
