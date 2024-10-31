package com.ejemplo.miproyecto.repositories;

import com.ejemplo.miproyecto.models.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterRepository extends MongoRepository<Counter, String> {
    Counter findByName(String name);
}
