package com.Estaciones.Estaciones.Classes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ESTACIONES")
@Getter
@Setter
@NoArgsConstructor
public class Estacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    private int id;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "FECHA_HORA_CREACION")
    private LocalDateTime fecha_hora_creacion;

    @Column(name = "LATITUD")
    private double latitud;

    @Column(name = "LONGITUD")
    private double longitud;


    public Estacion(String nombre, LocalDateTime fecha_hora_creacion, double latitud, double longitud) {
        this.nombre = nombre;
        this.fecha_hora_creacion = fecha_hora_creacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
