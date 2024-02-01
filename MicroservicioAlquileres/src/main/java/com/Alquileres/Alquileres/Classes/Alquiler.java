package com.Alquileres.Alquileres.Classes;

import com.Alquileres.Alquileres.Services.dtos.AlquilerDTO;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Table(name = "Alquileres")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ID_CLIENTE")
    private String id_cliente;

    @Column(name = "ESTADO")
    private Integer estado;

    @Column(name = "ESTACION_RETIRO")
    private Long estacionRetiro;

    @Column(name = "ESTACION_DEVOLUCION")
    private Long estacionDevolucion;

    @Column(name = "FECHA_HORA_RETIRO")
    private LocalDateTime fecha_hora_retiro;

    @Column(name = "FECHA_HORA_DEVOLUCION")
    private LocalDateTime fecha_hora_devolucion;

    @Column(name = "MONTO")
    private Double monto;

    @ManyToOne
    @JoinColumn(name = "ID_TARIFA", referencedColumnName = "ID")
    private Tarifa tarifa;

    @Transient
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Iniciar el alquiler
    public Alquiler(String id_cliente, Long estacionRetiro){
        this.id_cliente = id_cliente;
        this.estado = 1;
        this.estacionRetiro = estacionRetiro;
        this.estacionDevolucion = null;
        this.fecha_hora_retiro = LocalDateTime.now();
        this.monto = null;
        this.tarifa = null;
    }

    public Alquiler update(Long id, Alquiler alquiler){
        this.id = id;
        this.id_cliente = alquiler.getId_cliente();
        this.estado = 2;
        this.estacionRetiro = alquiler.getEstacionRetiro();
        this.estacionDevolucion = alquiler.getEstacionDevolucion();
        this.fecha_hora_retiro = alquiler.getFecha_hora_retiro();
        this.fecha_hora_devolucion = alquiler.getFecha_hora_devolucion();
        this.monto = alquiler.getMonto();
        this.tarifa = alquiler.getTarifa();

        return this;
    }
}
