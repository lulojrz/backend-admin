package com.example.back_admin.Controller;

import com.example.back_admin.Model.JwtUtil;
import com.example.back_admin.Model.Usuario;
import com.example.back_admin.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/verificacion/{id}")
    public String verificarContraseña(@PathVariable Integer id, @RequestBody Map<String, String> body) {

        String passwordEntrante = body.get("password");

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));




        if (passwordEncoder.matches(passwordEntrante, usuarioExistente.getPassword())) {
            return "exito";
        } else {
            return "fallo";
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public Optional<Usuario> eliminarUsuario(@PathVariable Integer id){
        Optional<Usuario> usuario_a_eliminar = usuarioRepository.findById(id);
        usuarioRepository.deleteById(id);
        return usuario_a_eliminar;
    }

    @PutMapping("/editar/usuario/{id}")
    public Usuario editarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioNuevo) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setUsuario(usuarioNuevo.getUsuario());
            usuarioExistente.setEmail(usuarioNuevo.getEmail());
            usuarioExistente.setRol(usuarioNuevo.getRol());
            usuarioExistente.setNombre(usuarioNuevo.getNombre());
            usuarioExistente.setApellido(usuarioNuevo.getApellido());


            String passRecibida = usuarioNuevo.getPassword();


            if (passRecibida == null || passRecibida.trim().isEmpty()) {

            } else if (!passRecibida.startsWith("$2a$")) {
                usuarioExistente.setPassword(passwordEncoder.encode(passRecibida));
            }


            return usuarioRepository.save(usuarioExistente);
        }).orElseThrow(() -> new RuntimeException("No existe el usuario"));
    }


    @GetMapping("/usuarios")
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }
    @PostMapping("/find/usuarios")
    public Optional<Usuario> encontrarUsuario(@RequestBody Usuario usuario){
        String nombre_usuario = usuario.getUsuario();
       return  usuarioRepository.findByUsuario(nombre_usuario);
    }

    @PostMapping("/registro")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {

        String passwordPlana = usuario.getPassword();


        String passwordHasheada = passwordEncoder.encode(passwordPlana);


        usuario.setPassword(passwordHasheada);


            return usuarioRepository.save(usuario);



    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario loginRequest) {

        Optional<Usuario> encontrado = usuarioRepository.findByUsuario(loginRequest.getUsuario());



        if (encontrado.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), encontrado.get().getPassword())) {

            String token = jwtUtil.generateToken(loginRequest.getUsuario());


            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("usuario", loginRequest.getUsuario());
            response.put("rol",loginRequest.getRol());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }



    }


}