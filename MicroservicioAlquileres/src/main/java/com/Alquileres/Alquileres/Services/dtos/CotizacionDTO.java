package com.Alquileres.Alquileres.Services.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CotizacionDTO {
    @JsonProperty("moneda_destino")
    private String moneda_destino;
    private Double importe;

    public CotizacionDTO(){

    }

    public CotizacionDTO(String moneda_destino, Double importe){
        this.moneda_destino = moneda_destino;
        this.importe = importe;
    }
}
