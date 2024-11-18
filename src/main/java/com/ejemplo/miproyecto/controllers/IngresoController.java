package com.ejemplo.miproyecto.controllers;

import com.ejemplo.miproyecto.models.Ingreso;
import com.ejemplo.miproyecto.models.Product;
import com.ejemplo.miproyecto.repositories.IngresoRepository;
import com.ejemplo.miproyecto.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class IngresoController {

    @Autowired
    private IngresoRepository ingresoRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/ingresos")
    public ResponseEntity<?> crearIngresos(@RequestBody List<Ingreso> ingresos) {
        for (Ingreso ingreso : ingresos) {
            Product producto = productRepository.findByNombre(ingreso.getProductoNombre());
            if (producto == null) {
                return ResponseEntity.status(400).body("Producto no encontrado: " + ingreso.getProductoNombre());
            }

            if (producto.getCantidad() < ingreso.getCantidad()) {
                return ResponseEntity.status(400).body("Cantidad insuficiente en el inventario para el producto: " + ingreso.getProductoNombre());
            }

            double totalVenta = ingreso.getCantidad() * producto.getPrecioDeVenta();
            producto.setCantidad(producto.getCantidad() - ingreso.getCantidad());
            productRepository.save(producto);

            ingreso.setFecha(LocalDateTime.now());
            ingreso.setTotal(totalVenta);
            ingresoRepository.save(ingreso);
        }
        return ResponseEntity.status(201).body("Ingresos creados exitosamente");
    }

    @GetMapping("/ingresos")
    public ResponseEntity<List<Ingreso>> obtenerIngresos() {
        List<Ingreso> ingresos = ingresoRepository.findAll();
        return ResponseEntity.ok(ingresos);
    }

    @PutMapping("/ingresos/{id}")
    public ResponseEntity<?> actualizarIngreso(@PathVariable String id, @RequestBody Ingreso ingresoActualizado) {
        // Buscar el ingreso por ID
        Ingreso ingresoExistente = ingresoRepository.findById(id).orElse(null);
        if (ingresoExistente == null) {
            return ResponseEntity.status(404).body("Ingreso no encontrado");
        }

        // Obtener el producto relacionado al ingreso
        Product producto = productRepository.findByNombre(ingresoExistente.getProductoNombre());
        if (producto == null) {
            return ResponseEntity.status(400).body("Producto no encontrado: " + ingresoExistente.getProductoNombre());
        }

        // Revertir el inventario del ingreso anterior
        producto.setCantidad(producto.getCantidad() + ingresoExistente.getCantidad());

        // Verificar si hay suficiente inventario para la nueva cantidad
        if (producto.getCantidad() < ingresoActualizado.getCantidad()) {
            return ResponseEntity.status(400).body("Cantidad insuficiente en el inventario para el producto: " + ingresoExistente.getProductoNombre());
        }

        // Actualizar el inventario con la nueva cantidad
        producto.setCantidad(producto.getCantidad() - ingresoActualizado.getCantidad());
        productRepository.save(producto);

        // Actualizar solo la fecha y la cantidad del ingreso
        ingresoExistente.setCantidad(ingresoActualizado.getCantidad());
        ingresoExistente.setFecha(ingresoActualizado.getFecha());
        ingresoExistente.setTotal(ingresoActualizado.getCantidad() * producto.getPrecioDeVenta());
        ingresoRepository.save(ingresoExistente);

        return ResponseEntity.ok("Ingreso actualizado exitosamente");
    }


    @DeleteMapping("/ingresos/{id}")
    public ResponseEntity<?> eliminarIngreso(@PathVariable String id) {
        // Buscar el ingreso por ID
        Ingreso ingreso = ingresoRepository.findById(id).orElse(null);
        if (ingreso == null) {
            return ResponseEntity.status(404).body("Ingreso no encontrado");
        }

        // Revertir el inventario del producto
        Product producto = productRepository.findByNombre(ingreso.getProductoNombre());
        if (producto != null) {
            producto.setCantidad(producto.getCantidad() + ingreso.getCantidad());
            productRepository.save(producto);
        }

        // Eliminar el ingreso
        ingresoRepository.delete(ingreso);
        return ResponseEntity.ok("Ingreso eliminado exitosamente");
    }

}
