package com.ejemplo.miproyecto.repositories;

import com.ejemplo.miproyecto.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Category findByNombre(String nombre);
}
