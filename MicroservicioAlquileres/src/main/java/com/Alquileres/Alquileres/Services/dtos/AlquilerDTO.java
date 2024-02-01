package com.Alquileres.Alquileres.Services.dtos;

import com.Alquileres.Alquileres.Classes.Alquiler;
import com.Alquileres.Alquileres.Classes.Tarifa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlquilerDTO {
    private Long id;
    private String id_cliente;
    private Integer estado;
    private Long estacion_retiro;
    private Long estacion_devolucion;
    private LocalDateTime fecha_hora_retiro;
    private LocalDateTime fecha_hora_devolucion;
    private Double monto;
    private Tarifa tarifa;
    private String divisa;

    public AlquilerDTO(Alquiler alquiler){
        this.id = alquiler.getId();
        this.id_cliente = alquiler.getId_cliente();
        this.estado = alquiler.getEstado();
        this.estacion_retiro = alquiler.getEstacionRetiro();
        this.estacion_devolucion = alquiler.getEstacionDevolucion();
        this.fecha_hora_retiro = alquiler.getFecha_hora_retiro();
        this.fecha_hora_devolucion = alquiler.getFecha_hora_devolucion();
        this.monto = alquiler.getMonto();
        this.tarifa = alquiler.getTarifa();
    }

    public AlquilerDTO(Alquiler alquiler, Double monto, String divisa){
        this.id = alquiler.getId();
        this.id_cliente = alquiler.getId_cliente();
        this.estado = 2;
        this.estacion_retiro = alquiler.getEstacionRetiro();
        this.estacion_devolucion = alquiler.getEstacionDevolucion();
        this.fecha_hora_retiro = alquiler.getFecha_hora_retiro();
        this.fecha_hora_devolucion = alquiler.getFecha_hora_devolucion();
        this.monto = monto;
        this.tarifa = alquiler.getTarifa();

        this.divisa = divisa;
    }
}
