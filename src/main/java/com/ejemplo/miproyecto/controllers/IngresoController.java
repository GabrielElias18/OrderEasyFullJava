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
            // Verificar si el producto existe por nombre
            Product producto = productRepository.findByNombre(ingreso.getProductoNombre());
            if (producto == null) {
                return ResponseEntity.status(400).body("Producto no encontrado: " + ingreso.getProductoNombre());
            }

            // Verificar si hay suficiente inventario para la venta
            if (producto.getCantidad() < ingreso.getCantidad()) {
                return ResponseEntity.status(400).body("Cantidad insuficiente en el inventario para el producto: " + ingreso.getProductoNombre());
            }

            // Calcular el total utilizando el precio de venta del producto
            double totalVenta = ingreso.getCantidad() * producto.getPrecioDeVenta();

            // Actualizar el inventario del producto
            producto.setCantidad(producto.getCantidad() - ingreso.getCantidad());
            productRepository.save(producto);

            // Guardar el ingreso (venta)
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
}
