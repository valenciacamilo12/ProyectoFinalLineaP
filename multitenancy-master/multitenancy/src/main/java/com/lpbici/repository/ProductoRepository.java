package com.lpbici.repository;

import com.lpbici.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Query("SELECT p FROM Producto p WHERE p.nombre = :nombre")
    Optional<Producto> findByNombre(@Param("nombre") String nombre);
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Producto p WHERE p.nombre = :nombre")
    boolean existsByNombre(@Param("nombre") String nombre);
}
