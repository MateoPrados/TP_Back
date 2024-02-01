package com.Alquileres.Alquileres.Services;

import com.Alquileres.Alquileres.Classes.Alquiler;
import com.Alquileres.Alquileres.Classes.Tarifa;
import com.Alquileres.Alquileres.Repositories.AlquileresRepository;
import com.Alquileres.Alquileres.Repositories.TarifasRepository;
import com.Alquileres.Alquileres.Services.ExternalServices.CotizacionMonedaInterface;
import com.Alquileres.Alquileres.Services.ExternalServices.EstacionServiceInterface;
import com.Alquileres.Alquileres.Services.dtos.AlquilerDTO;
import com.Alquileres.Alquileres.Services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AlquilerService {

    private final AlquileresRepository alquileresRepository;

    private final TarifasRepository tarifasRepository;

    private final EstacionServiceInterface estacionServiceInterface;

    private final CotizacionMonedaInterface cotizacionMonedaInterface;

    public AlquilerService(AlquileresRepository alquileresRepository,
                           TarifasRepository tarifasRepository,
                           EstacionServiceInterface estacionServiceInterface,
                           CotizacionMonedaInterface cotizacionMonedaInterface) {
        this.alquileresRepository = alquileresRepository;
        this.tarifasRepository = tarifasRepository;
        this.estacionServiceInterface = estacionServiceInterface;
        this.cotizacionMonedaInterface = cotizacionMonedaInterface;
    }

    /**
     * Busca un alquiler por su identificador.
     *
     * @param id Identificador del alquiler a buscar.
     * @return Un Optional que contiene un objeto AlquilerDTO si se encuentra el alquiler, o un Optional vacío si no se encuentra.
     */
    public Optional<AlquilerDTO> findById(Long id) {
        // Se busca el alquiler en el repositorio por su identificador
        Optional<Alquiler> existingAlquiler = alquileresRepository.findById(id);

        // Se verifica si el alquiler existe en el repositorio
        if (existingAlquiler.isEmpty()) {
            // Si no existe, se devuelve un Optional vacío
            return Optional.empty();
        } else {
            // Si existe, se devuelve un Optional que contiene un AlquilerDTO creado a partir del alquiler encontrado
            return Optional.of(new AlquilerDTO(existingAlquiler.get()));
        }
    }

    /**
     * Busca alquileres por estaciones de retiro y devolución.
     *
     * @param estacionRetiroId   Identificador de la estación de retiro.
     * @param estacionDevId      Identificador de la estación de devolución.
     * @return Lista de AlquilerDTO que coinciden con las estaciones de retiro y devolución proporcionadas.
     * @throws ResourceNotFoundException Si alguna de las estaciones no es válida.
     */
    public List<AlquilerDTO> buscarPorEstaciones(Long estacionRetiroId, Long estacionDevId) {
        // Verifica si la estación de retiro es válida
        Boolean estacionRetiro = controlEstacion(estacionRetiroId);
        if (!estacionRetiro) {
            throw new ResourceNotFoundException("La estación de retiro no es válida");
        }

        // Verifica si la estación de devolución es válida
        Boolean estacionDevolucion = controlEstacion(estacionDevId);
        if (!estacionDevolucion) {
            throw new ResourceNotFoundException("La estación de devolución no es válida");
        }

        // Busca alquileres en el repositorio que coincidan con las estaciones de retiro y devolución
        List<Alquiler> alquileres = alquileresRepository.findAllByEstacionRetiroAndEstacionDevolucion(estacionRetiroId, estacionDevId);

        // Utiliza map para convertir Alquiler a AlquilerDTO y toList() para recolectar los resultados en una lista

        return alquileres.stream()
                .map(AlquilerDTO::new)
                .toList();
    }


    /**
     * Crea un nuevo alquiler a partir de la información proporcionada.
     *
     * @param id_cliente      Identificador del cliente para el nuevo alquiler.
     * @param estacionRetiro  Identificador de la estación de retiro para el nuevo alquiler.
     * @return Un objeto AlquilerDTO que representa el alquiler recién creado y guardado.
     * @throws ResourceNotFoundException Si la estación de retiro no es válida.
     */
    public AlquilerDTO create(String id_cliente, Long estacionRetiro) {
        // Verifica si la estación de retiro es válida

        Boolean estacionRetiroValida = controlEstacion(estacionRetiro);
        if (!estacionRetiroValida) {
            throw new ResourceNotFoundException("La estación de retiro no es válida");
        }

        // Crea un nuevo objeto Alquiler a partir de la información proporcionada
        Alquiler alquiler = new Alquiler(id_cliente, estacionRetiro);

        // Guarda el nuevo alquiler en el repositorio
        Alquiler alquilerGuardado = alquileresRepository.save(alquiler);

        // Devuelve un AlquilerDTO que representa el alquiler recién creado y guardado
        return new AlquilerDTO(alquilerGuardado);
    }


    /**
     * Actualiza un alquiler con la estación de devolución, la fecha y hora de devolución, y calcula el monto.
     *
     * @param id                Identificador del alquiler a actualizar.
     * @param idEstacionDestino Identificador de la estación de devolución.
     * @param divisa            Divisa en la que se desea obtener el monto convertido (opcional).
     * @return Un objeto AlquilerDTO que representa el alquiler actualizado con el monto convertido.
     * @throws ResourceNotFoundException Si el alquiler no se encuentra o la estación de devolución no es válida.
     */
    public AlquilerDTO update(Long id, Long idEstacionDestino, String divisa) {
        // Buscar el alquiler por su identificador
        Alquiler alquiler = alquileresRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Alquiler [%d] inexistente", id))
        );

        // Verificar si la estación de devolución es válida
        Boolean estacionDevolucion = controlEstacion(idEstacionDestino);
        if (!estacionDevolucion) {
            throw new ResourceNotFoundException("La estación de devolución no es válida");
        }

        // Establecer la estación de devolución y la fecha y hora de devolución
        alquiler.setEstacionDevolucion(idEstacionDestino);
        LocalDateTime fechaDevolucion = LocalDateTime.now();
        alquiler.setFecha_hora_devolucion(fechaDevolucion);

        // Calcular el día de la semana y obtener la tarifa correspondiente
        int diaSemana = alquiler.getFecha_hora_devolucion().getDayOfWeek().getValue();
        LocalDateTime fechaRetiro = alquiler.getFecha_hora_retiro();
        Tarifa tarifaCorrespondiente = buscarTarifaCorresponde(fechaRetiro, diaSemana, alquiler, fechaDevolucion);

        // Calcular el monto del alquiler con la tarifa obtenida
        double monto = calcularCosto(tarifaCorrespondiente, fechaRetiro, fechaDevolucion, alquiler);
        alquiler.setMonto(monto);

        // Guardar el alquiler actualizado en la base de datos
        Alquiler alquilerFinal = alquileresRepository.save(alquiler.update(id, alquiler));

        // Calcular el monto convertido a la divisa especificada (o mantener en ARS si es nula o "ARS")
        Double montoConvertido = (divisa == null || "ARS".equals(divisa)) ?
                alquiler.getMonto() :
                cotizacionMonedaInterface.cotizarMoneda(alquilerFinal.getMonto(), divisa);

        // Retornar un objeto AlquilerDTO que representa el alquiler actualizado con el monto convertido
        return new AlquilerDTO(alquilerFinal, montoConvertido, divisa);
    }


    /**
     * Busca la tarifa correspondiente para el alquiler basándose en la fecha de devolución y el día de la semana.
     *
     * @param fechaRetiro      Fecha y hora de retiro del alquiler.
     * @param diaSemana        Día de la semana de la devolución.
     * @param alquiler              Objeto Alquiler para el cual se busca la tarifa correspondiente.
     * @param fechaDevolucion  Fecha y hora de devolución del alquiler.
     * @return La tarifa correspondiente al alquiler.
     */
    private Tarifa buscarTarifaCorresponde(LocalDateTime fechaRetiro, int diaSemana, Alquiler alquiler, LocalDateTime fechaDevolucion) {
        // Obtener la lista de tarifas de promoción para esa fecha de devolución
        List<Tarifa> tarifasPromocionales = tarifasRepository.findAll();

        // Variables para almacenar las tarifas coincidentes
        Tarifa tarifaSemana = null;
        Tarifa tarifaCoincidente = null;

        // Iterar sobre las tarifas para verificar coincidencias
        for (Tarifa tarifa : tarifasPromocionales) {
            if ("C".equals(tarifa.getDefinicion()) && coincideFechaDevolucionConTarifa(fechaRetiro, tarifa) != null) {
                // Si la tarifa es de tipo "C" y la fecha de devolución coincide, asignarla como tarifa coincidente
                tarifaCoincidente = tarifa;
                break; // Si encontramos una coincidencia, salimos del bucle
            } else if (!"C".equals(tarifa.getDefinicion()) && diaSemana == tarifa.getDia_semana()) {
                // Si la tarifa no es de tipo "C" y el día de la semana coincide, asignarla como tarifa semanal
                tarifaSemana = tarifa;
            }
        }

        // Asignar la tarifa correspondiente al alquiler
        if (tarifaCoincidente != null) {
            alquiler.setTarifa(tarifaCoincidente);
        } else {
            alquiler.setTarifa(tarifaSemana);
        }

        // Retornar la tarifa asignada al alquiler
        return alquiler.getTarifa();
    }


    /**
     * Verifica si la fecha de devolución coincide con la fecha especificada en una tarifa.
     *
     * @param fechaRetiro Fecha y hora de retiro del alquiler.
     * @param tarifa      Tarifa con la fecha a comparar.
     * @return La tarifa si la fecha de devolución coincide; de lo contrario, null.
     */
    private Tarifa coincideFechaDevolucionConTarifa(LocalDateTime fechaRetiro, Tarifa tarifa) {
        // Obtener los componentes de la fecha de la tarifa
        int diaTarifa = tarifa.getDia_mes();
        int mesTarifa = tarifa.getMes();
        int anioTarifa = tarifa.getAnio();

        // Obtener los componentes de la fecha de devolución
        int diaDevolucion = fechaRetiro.getDayOfMonth();
        int mesDevolucion = fechaRetiro.getMonthValue();
        int anioDevolucion = fechaRetiro.getYear();

        // Verificar si la fecha de devolución coincide con la fecha de la tarifa
        if (diaDevolucion == diaTarifa && mesDevolucion == mesTarifa && anioDevolucion == anioTarifa) {
            return tarifa;
        } else {
            return null;
        }
    }

    /**
     * Calcula el costo total del alquiler basándose en la tarifa, la fecha de retiro, la fecha de devolución y la distancia entre estaciones.
     *
     * @param tarifa           Tarifa a aplicar para el cálculo del costo.
     * @param fechaRetiro      Fecha y hora de retiro del alquiler.
     * @param fechaDevolucion  Fecha y hora de devolución del alquiler.
     * @param alq              Objeto Alquiler que contiene información sobre las estaciones de retiro y devolución.
     * @return El costo total del alquiler.
     */
    private Double calcularCosto(Tarifa tarifa, LocalDateTime fechaRetiro, LocalDateTime fechaDevolucion, Alquiler alq) {
        // Calcular la diferencia de tiempo en minutos entre la fecha de retiro y la fecha de devolución
        long minutosTotales = ChronoUnit.MINUTES.between(fechaRetiro, fechaDevolucion);

        // Calcular las horas completas y los minutos restantes, redondeando hacia arriba si los minutos restantes son mayores o iguales a 30
        long horasCompletas = minutosTotales / 60;
        long minutosRestantes = minutosTotales % 60;
        if (minutosRestantes >= 30) {
            minutosRestantes = 0;
            horasCompletas += 1;
        }

        // Calcular la distancia entre estaciones y el costo por kilómetro
        double costoPorKm = estacionServiceInterface.getDistancia(alq.getEstacionRetiro(), alq.getEstacionDevolucion());
        double costoDistancia = costoPorKm * tarifa.getMonto_km();

        // Calcular el costo total del alquiler
        double costoTotal = tarifa.getMonto_fijo_alquiler() + (horasCompletas * tarifa.getMonto_hora())
                + (minutosRestantes * tarifa.getMonto_minuto_fraccion()) + costoDistancia;

        return costoTotal;
    }


    /**
     * Verifica la existencia de una estación con el identificador especificado.
     *
     * @param id Identificador de la estación a verificar.
     * @return true si la estación existe, false si no.
     */
    public Boolean controlEstacion(Long id) {
        // Utiliza el servicio de estación para verificar si existe una estación con el identificador dado
        return estacionServiceInterface.existeIdEstacion(id);
    }

    /**
     * Verifica la existencia de una tarifa con el identificador especificado y la devuelve.
     *
     * @param id Identificador de la tarifa a verificar.
     * @return La tarifa correspondiente al identificador dado.
     * @throws ResourceNotFoundException Si la tarifa no existe.
     */
    public Tarifa controlTarifa(Long id) {
        // Busca la tarifa por su identificador en el repositorio, o lanza una excepción si no se encuentra

        // Devuelve la tarifa encontrada
        return tarifasRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Tarifa [%d] inexistente", id))
        );
    }

    public List<AlquilerDTO> getAll() {
        return alquileresRepository.findAll().stream().map(AlquilerDTO::new).toList();
    }
}