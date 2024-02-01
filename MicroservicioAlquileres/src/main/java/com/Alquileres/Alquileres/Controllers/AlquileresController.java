package com.Alquileres.Alquileres.Controllers;

import com.Alquileres.Alquileres.Services.AlquilerService;
import com.Alquileres.Alquileres.Services.dtos.*;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/microservicio/alquileres")
@RestController
public class AlquileresController {

    private final AlquilerService alquilerService;

    public AlquileresController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }


    @GetMapping("/listado")
    public ResponseEntity<List<AlquilerDTO>> getAll() {
        List<AlquilerDTO> alquileres = alquilerService.getAll();
        return ResponseEntity.ok(alquileres);
    }

    @PostMapping("/nuevo")
    public ResponseEntity<AlquilerDTO> startAlquiler(
            @RequestParam String id_cliente,
            @RequestParam Long estacionRetiro){
        AlquilerDTO alquilerCreado = alquilerService.create(id_cliente, estacionRetiro);
        return ResponseEntity.ok(alquilerCreado);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
            @RequestParam Long idEstacionDestino, @RequestParam(required = false) String divisa) {
        // Devuelve una respuesta de error si la divisa no es válida
        if (!esDivisaValida(divisa)) return ResponseEntity.badRequest().body("Debe elegir una moneda válida");
        // Validar que el idEstacionDestino no sea nulo
        if (idEstacionDestino == null)
            return ResponseEntity.badRequest().body("El parámetro idEstacionDestino no puede ser nulo");

        // Devuelve una respuesta exitosa con la entidad Alquiler actualizada
        AlquilerDTO alquilerDto = alquilerService.update(id, idEstacionDestino, divisa);

        return ResponseEntity.ok(alquilerDto);
    }

    // Método para verificar si la divisa es válida
    private boolean esDivisaValida(String divisa) {
        List<String> divisasPermitidas = Arrays.asList("EUR", "CLP", "BRL", "COP", "PEN", "GBP", "USD", "ARS");
        return divisa == null || divisasPermitidas.contains(divisa);
    }

    @GetMapping("/estaciones")
    public ResponseEntity<List<AlquilerDTO>> buscarAlquileresPorEstaciones(
            @RequestParam Long estacionRetiroId,
            @RequestParam Long estacionDevId) {
        List<AlquilerDTO> alquileres = alquilerService.buscarPorEstaciones(estacionRetiroId, estacionDevId);
        return new ResponseEntity<>(alquileres, HttpStatus.OK);
    }
}
