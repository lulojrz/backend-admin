package com.example.back_admin.Repository;

import com.example.back_admin.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente,Integer> {
    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);

    Optional<Cliente> findByUsuario(String usuario);
}
