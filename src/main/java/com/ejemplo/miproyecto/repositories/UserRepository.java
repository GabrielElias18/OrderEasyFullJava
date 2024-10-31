package com.ejemplo.miproyecto.repositories;

import com.ejemplo.miproyecto.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
