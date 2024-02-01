package com.Alquileres.Alquileres.Services.ExternalServices;

import com.Alquileres.Alquileres.Services.dtos.EstacionesDTO;

public interface EstacionServiceInterface {
    Boolean existeIdEstacion(Long id);
    Double getDistancia(Long idEstacionOrigen, Long idEstacionDestino);
}
