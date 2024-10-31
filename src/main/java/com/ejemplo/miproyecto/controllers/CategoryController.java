package com.ejemplo.miproyecto.controllers;

import com.ejemplo.miproyecto.models.Category;
import com.ejemplo.miproyecto.models.Counter;
import com.ejemplo.miproyecto.repositories.CategoryRepository;
import com.ejemplo.miproyecto.repositories.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CounterRepository counterRepository;

    @PostMapping("/categorias")
    public ResponseEntity<?> createCategory(@RequestBody Category category, HttpServletRequest request) {
        if (request.getAttribute("usuarioId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }

        int usuarioId = (int) request.getAttribute("usuarioId");

        try {
            Counter counter = counterRepository.findByName("categoriaId");
            if (counter == null) {
                counter = new Counter();
                counter.setName("categoriaId");
                counter.setValue(0);
            }
            counter.setValue(counter.getValue() + 1);
            counterRepository.save(counter);

            category.setCategoriaId(counter.getValue());
            category.setUsuarioId(usuarioId);
            categoryRepository.save(category);

            return ResponseEntity.status(HttpStatus.CREATED).body("Categoría creada exitosamente");
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear la categoría", e);
        }
    }
}
