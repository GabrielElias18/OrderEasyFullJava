package com.ejemplo.miproyecto.controllers;

import com.ejemplo.miproyecto.models.User;
import com.ejemplo.miproyecto.models.Counter;
import com.ejemplo.miproyecto.repositories.UserRepository;
import com.ejemplo.miproyecto.repositories.CounterRepository;
import com.ejemplo.miproyecto.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
            String token = jwtTokenUtil.generateToken(foundUser);
            return ResponseEntity.ok(Map.of("message", "Inicio de sesión exitoso", "token", token));
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(400).body("El nombre de usuario ya está en uso");
        }

        // Obtener el contador actual de usuarioId y actualizarlo
        Counter counter = counterRepository.findByName("usuarioId");
        if (counter == null) {
            counter = new Counter();
            counter.setName("usuarioId");
            counter.setValue(0);
        }
        counter.setValue(counter.getValue() + 1);
        counterRepository.save(counter);

        user.setUsuarioId(counter.getValue());
        userRepository.save(user);
        return ResponseEntity.status(201).body("Usuario registrado exitosamente");
    }
}
