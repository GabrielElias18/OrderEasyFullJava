package com.ejemplo.miproyecto.repositories;

import com.ejemplo.miproyecto.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByNombre(String nombre);
}
