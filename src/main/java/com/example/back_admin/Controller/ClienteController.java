package com.example.back_admin.Controller;


import com.example.back_admin.Model.Cliente;
import com.example.back_admin.Model.Usuario;
import com.example.back_admin.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public ResponseEntity<String> inicioSesion(@RequestBody Cliente usuario) {
        Optional<Cliente> Usuario_encontrado = clienteRepository.findByUsuario(usuario.getUsuario());

        if (Usuario_encontrado.isPresent()) {
            Cliente usuario_db = Usuario_encontrado.get();

            if (passwordEncoder.matches(usuario.getPassword(), usuario_db.getPassword())) {
                return ResponseEntity.ok("login exitoso"); // Status 200
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("contraseña incorrecta"); // Status 401
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado"); // Status 404
        }
    }
}
