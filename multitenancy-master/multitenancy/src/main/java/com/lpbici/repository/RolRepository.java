package com.lpbici.repository;

import com.lpbici.entity.Rol;
import enums.RolNombre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    @Query("SELECT r FROM Rol r WHERE r.rolNombre = :rolNombre")
    Optional<Rol> findByRolNombre(@Param("rolNombre") RolNombre rolNombre);
     @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rol r WHERE r.rolNombre = :rolNombre")
    boolean existsByRolNombre(@Param("rolNombre") RolNombre rolNombre);
}
