package com.Alquileres.Alquileres.Classes;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Table(name = "TARIFAS")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TIPO_TARIFA")
    private int tipo_tarifa;

    @Column(name = "DEFINICION")
    private String definicion;

    @Column(name = "DIA_SEMANA")
    private Integer dia_semana;

    @Column(name = "DIA_MES")
    private Integer dia_mes;

    @Column(name = "MES")
    private Integer mes;

    @Column(name = "ANIO")
    private Integer anio;

    @Column(name = "MONTO_FIJO_ALQUILER")
    private double monto_fijo_alquiler;

    @Column(name = "MONTO_MINUTO_FRACCION")
    private double monto_minuto_fraccion;

    @Column(name = "MONTO_KM")
    private double monto_km;

    @Column(name = "MONTO_HORA")
    private double monto_hora;
}

