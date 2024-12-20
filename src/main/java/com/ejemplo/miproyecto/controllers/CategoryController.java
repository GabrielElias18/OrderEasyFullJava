package com.ejemplo.miproyecto.controllers;

import com.ejemplo.miproyecto.models.Category;
import com.ejemplo.miproyecto.models.Counter;
import com.ejemplo.miproyecto.repositories.CategoryRepository;
import com.ejemplo.miproyecto.repositories.CounterRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CounterRepository counterRepository;

    @PostMapping("/categorias")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
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
            categoryRepository.save(category);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Categoría creada exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // Devolver JSON
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear la categoría", e);
        }
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryRepository.findAll();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener las categorías", e);
        }
    }

        // API para editar categoría
        @PutMapping("/categorias/{nombre}")
        public ResponseEntity<?> updateCategory(@PathVariable String nombre, @RequestBody Category updatedCategory) {
            try {
                Category existingCategory = categoryRepository.findByNombre(nombre);
                if (existingCategory == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada");
                }
    
                // Actualizar los campos necesarios
                existingCategory.setNombre(updatedCategory.getNombre());
                existingCategory.setDescripcion(updatedCategory.getDescripcion());
    
                categoryRepository.save(existingCategory);
    
                Map<String, String> response = new HashMap<>();
                response.put("message", "Categoría actualizada exitosamente");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al actualizar la categoría", e);
            }
        }
    
        // API para eliminar categoría
        @DeleteMapping("/categorias/{nombre}")
        public ResponseEntity<?> deleteCategory(@PathVariable String nombre) {
            try {
                Category category = categoryRepository.findByNombre(nombre);
                if (category == null) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoría no encontrada");
                }
    
                categoryRepository.delete(category);
    
                Map<String, String> response = new HashMap<>();
                response.put("message", "Categoría eliminada exitosamente");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Error al eliminar la categoría", e);
            }
        }
    }
