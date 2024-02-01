package com.Estaciones.Estaciones.Services;

import com.Estaciones.Estaciones.Classes.Estacion;
import com.Estaciones.Estaciones.Repositories.EstacionesRepository;
import com.Estaciones.Estaciones.Services.dtos.EstacionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Optional;

@Service
public class EstacionesService {

    private final EstacionesRepository estacionRepository;

    @Autowired
     public EstacionesService(EstacionesRepository estacionRepository){
         this.estacionRepository = estacionRepository;
     }

     public List<EstacionDTO> findAll() {              //estacion -> new EstacionDTO(estacion)
        return estacionRepository.findAll().stream().map(EstacionDTO::new).toList();
     }

     public Estacion findById(Long id) {
        Optional<Estacion> estacionOptional = estacionRepository.findById(id);

        if (estacionOptional.isPresent()) {
            Estacion encontrada = estacionOptional.get();
            return encontrada;
        } else return null;
     }

     public EstacionDTO findNearest(double latitud, double longitud) {

        List<Estacion> estaciones = estacionRepository.findAll();
        Estacion nearest = estaciones.get(0);

        double menorDistancia = calcularDistancia(latitud, longitud, nearest.getLatitud(), nearest.getLongitud());

        for (Estacion estacion : estaciones) {
            double distancia = calcularDistancia(latitud, longitud, estacion.getLatitud(), estacion.getLongitud());

            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                nearest = estacion;
            }
        }

        return new EstacionDTO(nearest);
     }

     public double getDistancia(Long id1, Long id2) {

        Estacion estacion1 = findById(id1);
        Estacion estacion2 = findById(id2);

        if (estacion1 == null || estacion2 == null) return -1;
        return calcularDistancia(estacion1.getLatitud(), estacion1.getLongitud(), estacion2.getLatitud(), estacion2.getLongitud());
     }

    public double calcularDistancia(double latitud1, double longitud1, double latitud2, double longitud2) {
        double x1 = latitud1 * 110000;
        double y1 = longitud1 * 110000;
        double x2 = latitud2 * 110000;
        double y2 = longitud2 * 110000;

        // Calcular la distancia euclidiana

        return Point2D.distance(x1, y1, x2, y2);
    }

    public Estacion addEstacion(EstacionDTO estacion) {
        Estacion nueva = new Estacion(estacion.getNombre(), estacion.getFecha_hora_creacion(), estacion.getLatitud(), estacion.getLongitud());
        estacionRepository.save(nueva);
        return nueva;
    }

    public EstacionDTO deleteEstacion(Long id) {

        Estacion estacion = findById(id);
        estacionRepository.delete(estacion);
        return new EstacionDTO(estacion);
    }


}
