package com.Estaciones.Estaciones.Controllers;

import com.Estaciones.Estaciones.Classes.Estacion;
import com.Estaciones.Estaciones.Services.EstacionesService;
import com.Estaciones.Estaciones.Services.dtos.EstacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/microservicio/estaciones")
@RestController
public class EstacionesController {

    private final EstacionesService estacionService;

    public EstacionesController(EstacionesService _estacionService) {
        estacionService = _estacionService;
    }

    @GetMapping("/listado")
    public ResponseEntity<List<EstacionDTO>> getEstaciones() {

        List<EstacionDTO> estaciones = estacionService.findAll();
        return ResponseEntity.ok(estaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstacionDTO> getEstacion(@PathVariable Long id) {
        Estacion encontrada = estacionService.findById(id);

        if (encontrada != null) {
            EstacionDTO estacion = new EstacionDTO(encontrada);
            return ResponseEntity.ok(estacion);
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping("/cercana")
    public ResponseEntity<EstacionDTO> getNearest(@RequestParam double latitud, @RequestParam double longitud) {
        EstacionDTO estacionMasCercana = estacionService.findNearest(latitud, longitud);
        return ResponseEntity.ok(estacionMasCercana);
    }

    @GetMapping("/distancia/{id1}/{id2}")
    public ResponseEntity<Double> getDistanciaEstaciones(@PathVariable Long id1, @PathVariable Long id2) {
        double distancia = estacionService.getDistancia(id1, id2); // creo que hay que dividirla por 1000 pero ni idea por que, despues lo veo

        if (distancia == -1) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(distancia);
    }

    @PostMapping("/agregar")
    public ResponseEntity<Estacion> addEstacion(@RequestBody EstacionDTO estacion) {
        Estacion nueva = estacionService.addEstacion(estacion);
        return ResponseEntity.ok(nueva);
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<EstacionDTO> deleteEstacion(@PathVariable Long id) {
        EstacionDTO estacion = estacionService.deleteEstacion(id);
        return ResponseEntity.ok(estacion);
    }

}
