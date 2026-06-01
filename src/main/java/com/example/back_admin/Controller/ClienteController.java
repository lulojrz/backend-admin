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

    @GetMapping("/{id}")
    public Optional<Cliente> obtenerCliente(@PathVariable Integer id){
       return  clienteRepository.findById(id);
    }
    @PostMapping("verificar/{id}")
    public String verificarContraseña(@PathVariable Integer id, @RequestBody Map<String, String> body){
        String passwordEntrante = body.get("password");

        Cliente cliente_existente= clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));


        if (passwordEncoder.matches(passwordEntrante, cliente_existente.getPassword())) {
            return "exito";
        } else {
            return "fallo";
        }
    }
    @PostMapping(value = "/email", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> obtenerEmail(@RequestBody Map<String,String> payload) {
        try {
            // 1. Extraemos el usuario que viene en el JSON de React
            String usuario = payload.get("usuario");

            if (usuario == null || usuario.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: El campo 'usuario' es obligatorio");
            }

            // 2. Buscamos en la base de datos
            Optional<Cliente> cliente = clienteRepository.findByUsuario(usuario);

            // 3. Retornamos solo el String del email o el error correspondiente
            if (cliente.isPresent()) {
                return ResponseEntity.ok(cliente.get().getEmail()); // <-- Acá devuelve solo el texto del email
            } else {
                return ResponseEntity.status(404).body("Error: Usuario no encontrado");
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error en el servidor: " + e.getMessage());
        }
    }

    @PutMapping("/editar/{id}")
    public Cliente editarUsuario(@PathVariable Integer id, @RequestBody Cliente cliente_nuevo){
        return clienteRepository.findById(id).map(cliente ->
        {
            cliente.setUsuario(cliente_nuevo.getUsuario());
            cliente.setNombre(cliente_nuevo.getNombre());
            cliente.setApellido(cliente_nuevo.getApellido());
            cliente.setEmail(cliente_nuevo.getEmail());
            cliente.setPassword(cliente_nuevo.getPassword());
            cliente.setRol(cliente_nuevo.getRol());
            cliente.setTelefono(cliente_nuevo.getTelefono());
            cliente.setFecha(cliente_nuevo.getFecha());
            cliente.setActivo(cliente_nuevo.getActivo());

            String passRecibida = cliente_nuevo.getPassword();


            if (passRecibida == null || passRecibida.trim().isEmpty()) {

            } else if (!passRecibida.startsWith("$2a$")) {
                cliente.setPassword(passwordEncoder.encode(passRecibida));
            }
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("No existe el cliente"));

    }

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
            response.put("id", Integer.toString(encontrado.get().getId()));

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
            return ResponseEntity.ok(cliente.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
