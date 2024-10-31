package com.ejemplo.miproyecto.repositories;

import com.ejemplo.miproyecto.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByUsuarioId(int usuarioId);
}
