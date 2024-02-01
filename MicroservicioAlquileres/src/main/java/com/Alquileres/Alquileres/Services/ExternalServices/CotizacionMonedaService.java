package com.Alquileres.Alquileres.Services.ExternalServices;

import com.Alquileres.Alquileres.Services.dtos.CotizacionDTO;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.jetbrains.annotations.Debug;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CotizacionMonedaService implements CotizacionMonedaInterface {
    RestTemplate restTemplate;

    @Override
    public Double cotizarMoneda(Double monto, String monedaDestino) {
        // Realizar la llamada al servicio de cotización de moneda
        ResponseEntity<CotizacionDTO> response = realizarSolicitudCotizacion(monto, monedaDestino);

        // Verificar si la respuesta es exitosa y contiene datos
        if (esRespuestaExitosa(response)) {
            return response.getBody().getImporte();
        }

        // En caso de respuesta no exitosa o datos nulos, retornar null
        return null;
    }

    /**
     * Realiza la solicitud al servicio de cotización de moneda.
     *
     * @param monto         Monto a convertir.
     * @param monedaDestino Moneda de destino.
     * @return ResponseEntity con la respuesta del servicio.
     */
    private ResponseEntity<CotizacionDTO> realizarSolicitudCotizacion(Double monto, String monedaDestino) {
        // Construir el cuerpo de la solicitud
        CotizacionDTO cotizacionRequest = new CotizacionDTO(monedaDestino, monto);

        // Configurar los encabezados de la solicitud
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CotizacionDTO> requestEntity = new HttpEntity<>(cotizacionRequest, headers);

        // Realizar la solicitud al servicio de cotización
        return restTemplate.postForEntity(
                "http://34.82.105.125:8080/convertir",
                cotizacionRequest,
                CotizacionDTO.class
        );
    }

    /**
     * Verifica si la respuesta del servicio es exitosa y contiene datos.
     *
     * @param response ResponseEntity con la respuesta del servicio.
     * @return true si la respuesta es exitosa y contiene datos, false en caso contrario.
     */
    private boolean esRespuestaExitosa(ResponseEntity<CotizacionDTO> response) {
        return response.getStatusCode().is2xxSuccessful() && response.getBody() != null;
    }
}
