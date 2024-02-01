package com.Alquileres.Alquileres.Services.ExternalServices;

import com.Alquileres.Alquileres.Services.dtos.EstacionesDTO;

import lombok.experimental.FieldDefaults;
import lombok.val;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class EstacionService implements EstacionServiceInterface {


    private final RestTemplate restTemplate;

    public EstacionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean existeIdEstacion(Long id) {
        try {
            // Se utiliza RestTemplate para realizar una solicitud GET a la API de estaciones
            val response = restTemplate.getForEntity("http://localhost:8082/microservicio/estaciones/{id}", EstacionesDTO.class, id);

            // Se valida que el código de estado sea 2xx (éxito) y que el cuerpo de la respuesta no sea nulo
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return true;
            }
        } catch (Exception e) {
            // En caso de excepción, se imprime el mensaje de error y se devuelve falso
            System.out.println("Error al verificar la existencia de la estación con ID " + id + ": " + e.getMessage());
            return false;
        }

        // Si no se cumple ninguna condición, se devuelve falso por defecto
        return false;
    }


    @Override
    public Double getDistancia(Long idEstacionOrigen, Long idEstacionDestino) {
        try {
            // Se utiliza RestTemplate para realizar una solicitud GET a la API de estaciones para obtener la distancia entre dos estaciones
            val response = restTemplate.getForEntity("http://localhost:8082/microservicio/estaciones/distancia/{id1}/{id2}"
                    , Double.class, idEstacionOrigen, idEstacionDestino);

            // Se valida que el código de estado sea 2xx (éxito) y que el cuerpo de la respuesta no sea nulo
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }

        } catch (Exception e) {
            // En caso de excepción, se imprime el mensaje de error y se devuelve nulo
            System.out.println("Error al obtener la distancia entre las estaciones con IDs " + idEstacionOrigen + " y " + idEstacionDestino + ": " + e.getMessage());
            return null;
        }

        // Si no se cumple ninguna condición, se devuelve nulo por defecto
        return null;
    }
}
