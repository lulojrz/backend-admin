package com.example.back_admin.Controller;

import com.example.back_admin.Model.DetalleVentas;
import com.example.back_admin.Model.Producto_variantes;
import com.example.back_admin.Model.Venta;
import com.example.back_admin.Repository.Producto_variantesRepository;
import com.example.back_admin.Repository.VentasDetalleRepository;
import com.example.back_admin.Repository.VentasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

            System.out.println(">>> Iniciando registro de cabecera de venta para cliente ID: " + venta.getCliente().getId());

            // Aseguramos la fecha del servidor por si acaso
            if (venta.getFecha() == null) {
                venta.setFecha(java.time.LocalDateTime.now());
            }

            // Guardamos únicamente la cabecera en la tabla 'ventas'
            Venta ventaGuardada = ventasRepository.save(venta);
            System.out.println(">>> Cabecera de venta guardada exitosamente con ID: " + ventaGuardada.getId());

            // Importante: Devolvemos el objeto guardado completo (o al menos su ID)
            // para que desde React sepas qué 'venta_id' asignarle a los detalles después.
            return ResponseEntity.ok(ventaGuardada);

        } catch (Exception e) {
            System.err.println(">>> ERROR AL REGISTRAR CABECERA: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al registrar la venta: " + e.getMessage());
        }
    }

}
