package com.example.back_admin.Repository;

import com.example.back_admin.Model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriasRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findById(Long id);
}
