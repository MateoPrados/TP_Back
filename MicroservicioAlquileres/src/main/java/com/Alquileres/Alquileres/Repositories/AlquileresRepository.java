package com.Alquileres.Alquileres.Repositories;

import com.Alquileres.Alquileres.Classes.Alquiler;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlquileresRepository extends JpaRepository<Alquiler, Long> {
    List<Alquiler> findAllByEstacionRetiroAndEstacionDevolucion(Long retiroId, Long devolucionId);
}

