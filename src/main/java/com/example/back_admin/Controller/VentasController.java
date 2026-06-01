package com.example.back_admin.Controller;

import com.example.back_admin.Model.DetalleVentas;
import com.example.back_admin.Model.Venta;
import com.example.back_admin.Repository.Producto_variantesRepository;
import com.example.back_admin.Repository.VentasDetalleRepository;
import com.example.back_admin.Repository.VentasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/confirmar")
@CrossOrigin(origins = "*")

public class VentasController {

    @Autowired
    private VentasRepository ventasRepository;
    @Autowired
    private VentasDetalleRepository ventasDetalleRepository;
    @Autowired
    private Producto_variantesRepository productoVariantesRepository;

    @DeleteMapping("/borrar/venta/{id}")
    public Optional<Venta> borrarVenta(@PathVariable Integer id){
         Optional<Venta> ventaBorrada = ventasRepository.findById(id);
         ventasRepository.deleteById(id);
         return ventaBorrada;


    }

    @PostMapping("/venta")
    @Transactional // Sigue siendo buena práctica por si manejas otras operaciones de persistencia juntas
    public ResponseEntity<?> confirmarVenta(@RequestBody Venta venta) {
        try {
            // Validación básica de seguridad
            if (venta == null || venta.getCliente() == null || venta.getCliente().getId() == null) {
                return ResponseEntity.badRequest().body("Error: Los datos de la venta o el ID del cliente son nulos.");
            }

            // Aseguramos la fecha del servidor por si acaso
            if (venta.getFecha() == null) {
                venta.setFecha(java.time.LocalDateTime.now());
            }

            // Guardamos únicamente la cabecera en la tabla 'ventas'
            Venta ventaGuardada = ventasRepository.save(venta);

            // Importante: Devolvemos el objeto guardado completo (o al menos su ID)
            // para que desde React sepas qué 'venta_id' asignarle a los detalles después.
            return ResponseEntity.ok(ventaGuardada);

        } catch (Exception e) {
            System.err.println(">>> ERROR AL REGISTRAR CABECERA: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al registrar la venta: " + e.getMessage());
        }
    }

    @PostMapping("/detalles")
    @Transactional
    public ResponseEntity<?> enviarDetalles(@RequestBody List<DetalleVentas> detalles) {
        try {
            if (detalles == null || detalles.isEmpty()) {
                return ResponseEntity.badRequest().body("Error: La lista de detalles está vacía.");
            }

            for (DetalleVentas detalle : detalles) {
                ventasDetalleRepository.save(detalle);
            }

            return ResponseEntity.ok().body("{\"message\": \"Detalles registrados correctamente\"}");

        } catch (Exception e) {
            System.err.println(">>> ERROR EN DETALLES: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al guardar detalles: " + e.getMessage());
        }
    }

    @GetMapping("/ventasRealizadas")
    public List<Venta> obtenerVentas(){
        return ventasRepository.findAll();
    }
    @GetMapping("/venta/cliente/{id}")
    public List<Venta> obtenerVentaporCliente(@PathVariable Integer id) {

        return ventasRepository.findByClienteId(id);
    }
    @GetMapping("/obtenerDetalles/{id}")
    public List<DetalleVentas> obtenerDetallesVenta(@PathVariable Integer id){
        return  ventasDetalleRepository.obtenerDetalleporID(id);
    }

}
