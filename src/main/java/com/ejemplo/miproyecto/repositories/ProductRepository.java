package com.ejemplo.miproyecto.repositories;

import com.ejemplo.miproyecto.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByUsuarioId(int usuarioId);
}
