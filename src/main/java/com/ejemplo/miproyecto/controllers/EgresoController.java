package com.ejemplo.miproyecto.controllers;

import com.ejemplo.miproyecto.models.Egreso;
import com.ejemplo.miproyecto.models.Product;
import com.ejemplo.miproyecto.repositories.EgresoRepository;
import com.ejemplo.miproyecto.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EgresoController {

    @Autowired
    private EgresoRepository egresoRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/egresos")
    public ResponseEntity<?> crearEgresos(@RequestBody List<Egreso> egresos) {
        for (Egreso egreso : egresos) {
            // Verificar si el producto existe por nombre
            Product producto = productRepository.findByNombre(egreso.getProductoNombre());
            if (producto == null) {
                return ResponseEntity.status(400).body("Producto no encontrado: " + egreso.getProductoNombre());
            }

            // Calcular el total utilizando el precio de compra del producto
            double totalCompra = egreso.getCantidad() * producto.getPrecioDeCompra();

            // Actualizar el inventario del producto
            producto.setCantidad(producto.getCantidad() + egreso.getCantidad());
            productRepository.save(producto);

            // Guardar el egreso (compra)
            egreso.setFecha(LocalDateTime.now());
            egreso.setTotal(totalCompra);
            egresoRepository.save(egreso);
        }

        return ResponseEntity.status(201).body("Egresos creados exitosamente");
    }

    @GetMapping("/egresos")
    public ResponseEntity<List<Egreso>> obtenerEgresos() {
        List<Egreso> egresos = egresoRepository.findAll();
        return ResponseEntity.ok(egresos);
    }

    @PutMapping("/egresos/{id}")
    public ResponseEntity<?> actualizarEgreso(@PathVariable String id, @RequestBody Egreso egresoActualizado) {
        // Buscar el egreso por ID
        Egreso egresoExistente = egresoRepository.findById(id).orElse(null);
        if (egresoExistente == null) {
            return ResponseEntity.status(404).body("Egreso no encontrado");
        }

        // Obtener el producto relacionado al egreso
        Product producto = productRepository.findByNombre(egresoExistente.getProductoNombre());
        if (producto == null) {
            return ResponseEntity.status(400).body("Producto no encontrado: " + egresoExistente.getProductoNombre());
        }

        // Revertir el inventario del egreso anterior
        producto.setCantidad(producto.getCantidad() - egresoExistente.getCantidad());

        // Verificar si hay suficiente inventario para la nueva cantidad
        producto.setCantidad(producto.getCantidad() + egresoActualizado.getCantidad());  // Agregar la nueva cantidad
        productRepository.save(producto);

        // Actualizar el egreso con los nuevos valores
        egresoExistente.setCantidad(egresoActualizado.getCantidad());
        egresoExistente.setFecha(egresoActualizado.getFecha());
        egresoExistente.setTotal(egresoActualizado.getCantidad() * producto.getPrecioDeCompra());
        egresoRepository.save(egresoExistente);

        return ResponseEntity.ok("Egreso actualizado exitosamente");
    }

    @DeleteMapping("/egresos/{id}")
    public ResponseEntity<?> eliminarEgreso(@PathVariable String id) {
        // Buscar el egreso por ID
        Egreso egreso = egresoRepository.findById(id).orElse(null);
        if (egreso == null) {
            return ResponseEntity.status(404).body("Egreso no encontrado");
        }

        // Obtener el producto relacionado al egreso
        Product producto = productRepository.findByNombre(egreso.getProductoNombre());
        if (producto != null) {
            // Revertir la cantidad al inventario
            producto.setCantidad(producto.getCantidad() - egreso.getCantidad());
            productRepository.save(producto);
        }

        // Eliminar el egreso
        egresoRepository.delete(egreso);
        return ResponseEntity.ok("Egreso eliminado exitosamente");
    }
}
