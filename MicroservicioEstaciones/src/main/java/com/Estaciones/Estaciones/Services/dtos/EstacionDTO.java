package com.Estaciones.Estaciones.Services.dtos;

import com.Estaciones.Estaciones.Classes.Estacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstacionDTO {
    private int id;
    private String nombre;
    private LocalDateTime fecha_hora_creacion;
    private double latitud;
    private double longitud;

    // Mapear entidad a DTO
    public EstacionDTO(Estacion estacion) {
        this.id = estacion.getId();
        this.nombre = estacion.getNombre();
        this.fecha_hora_creacion = estacion.getFecha_hora_creacion();
        this.latitud = estacion.getLatitud();
        this.longitud = estacion.getLongitud();
    }

}
