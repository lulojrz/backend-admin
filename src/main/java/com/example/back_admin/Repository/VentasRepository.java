package com.example.back_admin.Repository;

import com.example.back_admin.Model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentasRepository extends JpaRepository<Venta,Integer> {
}
