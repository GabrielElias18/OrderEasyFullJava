package com.ejemplo.miproyecto.controllers;

import com.ejemplo.miproyecto.models.Product;
import com.ejemplo.miproyecto.models.Counter;
import com.ejemplo.miproyecto.repositories.ProductRepository;
import com.ejemplo.miproyecto.repositories.CategoryRepository;
import com.ejemplo.miproyecto.models.Category;
import com.ejemplo.miproyecto.repositories.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CounterRepository counterRepository;

    @PostMapping("/productos")
    public ResponseEntity<?> crearProducto(@RequestBody Product product) {
        Category category = categoryRepository.findByNombre(product.getCategoriaNombre());
        if (category == null) {
            return ResponseEntity.status(400).body("Categoría no encontrada");
        }

        Counter counter = counterRepository.findByName("productoId");
        if (counter == null) {
            counter = new Counter();
            counter.setName("productoId");
            counter.setValue(0);
        }
        counter.setValue(counter.getValue() + 1);
        counterRepository.save(counter);

        product.setProductoId(counter.getValue());
        productRepository.save(product);

        return ResponseEntity.status(201).body("Producto creado exitosamente");
    }

    @GetMapping("/productos")
    public ResponseEntity<List<Product>> obtenerProductos() {
        List<Product> productos = productRepository.findAll();
        return ResponseEntity.ok(productos);
    }

    @PutMapping("/productos/nombre/{nombre}")
    public ResponseEntity<?> actualizarProductoPorNombre(@PathVariable String nombre, @RequestBody Product productDetails) {
        Product product = productRepository.findByNombre(nombre);
        if (product == null) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        Category category = categoryRepository.findByNombre(productDetails.getCategoriaNombre());
        if (category == null) {
            return ResponseEntity.status(400).body("Categoría no encontrada");
        }

        product.setNombre(productDetails.getNombre());
        product.setDescripcion(productDetails.getDescripcion());
        product.setCantidad(productDetails.getCantidad());
        product.setCategoriaNombre(productDetails.getCategoriaNombre());
        product.setPrecioDeVenta(productDetails.getPrecioDeVenta());
        product.setPrecioDeCompra(productDetails.getPrecioDeCompra());
        productRepository.save(product);

        return ResponseEntity.ok("Producto actualizado exitosamente");
    }

    @DeleteMapping("/productos/nombre/{nombre}")
    public ResponseEntity<?> eliminarProductoPorNombre(@PathVariable String nombre) {
        Product product = productRepository.findByNombre(nombre);
        if (product == null) {
            return ResponseEntity.status(404).body("Producto no encontrado");
        }

        productRepository.delete(product);
        return ResponseEntity.ok("Producto eliminado exitosamente");
    }
}
