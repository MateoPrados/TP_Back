package com.Alquileres.Alquileres.Repositories;

import com.Alquileres.Alquileres.Classes.Tarifa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TarifasRepository extends JpaRepository<Tarifa, Long> {
}
