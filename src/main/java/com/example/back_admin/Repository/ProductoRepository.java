package com.example.back_admin.Repository;

import com.example.back_admin.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Long> {

    List<Producto> findByCategoriaId(Long categoriaId);


    List<Producto> findByMarca(String marca);
}
