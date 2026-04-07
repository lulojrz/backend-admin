package com.example.back_admin.Repository;


import com.example.back_admin.Model.Producto_variantes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Producto_variantesRepository extends JpaRepository<Producto_variantes,Long> {
    List<Producto_variantes> findByProductoId(Long productoId);
}
