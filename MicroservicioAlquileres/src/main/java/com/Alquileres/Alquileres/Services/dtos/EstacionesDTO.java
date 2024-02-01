package com.Alquileres.Alquileres.Services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstacionesDTO{
    private Long id;
    private String nombre;
    private Double latitud;
    private Double longitud;
}
