package com.Estaciones.Estaciones.Repositories;

import com.Estaciones.Estaciones.Classes.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstacionesRepository extends JpaRepository<Estacion, Long> {
}
