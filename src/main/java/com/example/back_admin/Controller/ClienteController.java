package com.example.back_admin.Controller;


import com.example.back_admin.Model.Cliente;
import com.example.back_admin.Model.JwtUtil;
import com.example.back_admin.Model.Usuario;
import com.example.back_admin.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("todos")
    public List<Cliente> obtenerClientes(){
        return clienteRepository.findAll();
    }
    @PostMapping("/agregar")
    public ResponseEntity<?> agregarCliente(@RequestBody Cliente cliente) {

        if (clienteRepository.existsByUsuario(cliente.getUsuario())) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está en uso.");
        }

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            return ResponseEntity.badRequest().body("Error: El email ya está registrado.");
        }


        String passwordHasheada = passwordEncoder.encode(cliente.getPassword());
        cliente.setPassword(passwordHasheada);


        cliente.setActivo(1);
        cliente.setFecha(LocalDateTime.now());


        clienteRepository.save(cliente);


        cliente.setPassword(null);
        return ResponseEntity.ok(cliente);
    }


    @DeleteMapping("/borrar/{id}")
    public String  borrarCliente(@PathVariable Integer id){
        if (id!=null){
         clienteRepository.deleteById(id);
          return "borrado exitosamente";
        }else{
            return "no se pudo borrar";
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> inicioSesion(@RequestBody Cliente usuario) {
        Optional<Cliente> encontrado = clienteRepository.findByUsuario(usuario.getUsuario());

        if (encontrado.isPresent() && passwordEncoder.matches(usuario.getPassword(), encontrado.get().getPassword())) {

            String token = jwtUtil.generateToken(usuario.getUsuario());


            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("usuario", usuario.getUsuario());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }


    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(Principal principal) {
        // Principal contiene el "subject" que pusimos en el token (el nombre de usuario)
        Optional<Cliente> cliente = clienteRepository.findByUsuario(principal.getName());
        if(cliente.isPresent()){
            cliente.get().setPassword(null);// Seguridad: nunca mandes el hash al front
            System.out.println("Datos del cliente a enviar: " + cliente.get().toString()); // Esto usa el @Data de Lombok
            return ResponseEntity.ok(cliente.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
