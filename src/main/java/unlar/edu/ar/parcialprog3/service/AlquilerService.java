package unlar.edu.ar.parcialprog3.service;

import unlar.edu.ar.parcialprog3.domain.*;
import unlar.edu.ar.parcialprog3.pattern.strategy.CalculadoraTarifaAdaptativa;
import unlar.edu.ar.parcialprog3.pattern.strategy.EstrategiaCalculoTarifa;
import unlar.edu.ar.parcialprog3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class AlquilerService {
    
    private final AlquilerRepository alquilerRepository;
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstacionAnclajRepository estacionRepository;
    private final PagoService pagoService;

    // Contexto del patrón STRATEGY: permite cambiar en caliente cómo se calcula
    // la tarifa (estándar, hora pico, climática) sin reiniciar la aplicación.
    private final CalculadoraTarifaAdaptativa calculadoraTarifa = new CalculadoraTarifaAdaptativa();

    /**
     * Cambia la estrategia de cálculo de tarifa activa (patrón STRATEGY).
     */
    public void cambiarEstrategiaTarifa(EstrategiaCalculoTarifa nuevaEstrategia) {
        calculadoraTarifa.cambiarEstrategia(nuevaEstrategia);
    }

    /**
     * Obtiene información legible de la estrategia de tarifa activa.
     */
    public String obtenerInfoEstrategiaTarifa() {
        return calculadoraTarifa.obtenerInfoEstrategia();
    }

    /**
     * Desbloquea un vehículo e inicia un alquiler
     */
    public Alquiler desbloquearVehiculo(Long idUsuario, String patente, Pago.MetodoPago metodoPago) 
            throws IllegalArgumentException {
        
        // Validar usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NoSuchElementException("Usuario No Encontrado"));
        
        // Buscar vehículo por patente
        Vehiculo vehiculo = vehiculoRepository.findByPatente(patente)
                .orElseThrow(() -> new NoSuchElementException("Vehículo No Encontrado"));
        
        // Validar que la batería sea apta (mínimo 15%)
        if (!vehiculo.validarBateria()) {
            throw new IllegalArgumentException("Batería Insuficiente");
        }

        // Validar y aplicar la transición de estado (patrón STATE).
        // intentarIniciarViaje() ya verifica que el vehículo esté EN_ESPERA
        // y que tenga batería suficiente; si no puede, no muta el estado.
        if (!vehiculo.intentarIniciarViaje()) {
            throw new IllegalArgumentException("El vehículo ya está siendo utilizado o no está disponible");
        }
        
        // Crear nuevo alquiler
        Alquiler alquiler = new Alquiler();
        alquiler.setUsuario(usuario);
        alquiler.setVehiculo(vehiculo);
        alquiler.setEstacionInicio(vehiculo.getEstacionActual());
        alquiler.setFechaInicio(LocalDateTime.now());
        alquiler.setEstado(Alquiler.EstadoAlquiler.ACTIVO);
        
        // Calcular tarifa inicial (costo del primer minuto, según la estrategia activa)
        Double tarifaInicial = calculadoraTarifa.calcularCosto(1, vehiculo.getValorTarifa());
        alquiler.setMontoTotal(tarifaInicial);
        
        // Aplicar descuento si es usuario Premium
        if (usuario.getTipoUsuario() == Usuario.TipoUsuario.PREMIUM) {
            alquiler.setDescuentoAplicado(15.0);
        }
        
        // Persistir el nuevo estado del vehículo (EN_VIAJE)
        vehiculoRepository.save(vehiculo);
        
        // Guardar alquiler
        return alquilerRepository.save(alquiler);
    }
    
    /**
     * Finaliza un alquiler y procesa el pago
     */
    public Alquiler finalizarAlquiler(Long idAlquiler) throws IllegalArgumentException {
        Alquiler alquiler = alquilerRepository.findById(idAlquiler)
                .orElseThrow(() -> new NoSuchElementException("Alquiler no encontrado"));
        
        if (!alquiler.getEstado().equals(Alquiler.EstadoAlquiler.ACTIVO)) {
            throw new IllegalArgumentException("El alquiler no está activo");
        }
        
        alquiler.setFechaFin(LocalDateTime.now());
        
        // Calcular duración y monto final
        calcularMontoFinal(alquiler);
        
        // Procesar pago
        Pago pago = pagoService.procesarPago(alquiler);
        
        if (pago.getEstado() == Pago.EstadoPago.EXITOSO) {
            alquiler.setEstado(Alquiler.EstadoAlquiler.FINALIZADO);
            alquiler.setMontoPagado(pago.getMonto());
            
            // Devolver el vehículo a EN_ESPERA (patrón STATE)
            Vehiculo vehiculo = alquiler.getVehiculo();
            if (!vehiculo.intentarFinalizarViaje()) {
                throw new IllegalArgumentException("El vehículo no se encuentra en un viaje activo");
            }
            vehiculoRepository.save(vehiculo);
        } else {
            throw new IllegalArgumentException("El pago no fue procesado exitosamente");
        }
        
        return alquilerRepository.save(alquiler);
    }
    
    /**
     * Calcula el monto final del alquiler usando la estrategia de tarifa activa
     */
    private void calcularMontoFinal(Alquiler alquiler) {
        if (alquiler.getFechaFin() == null) {
            return;
        }
        
        long minutos = java.time.temporal.ChronoUnit.MINUTES.between(
                alquiler.getFechaInicio(), 
                alquiler.getFechaFin()
        );
        
        Double tarifaPorMinuto = alquiler.getVehiculo().getValorTarifa();
        Double montoBase = calculadoraTarifa.calcularCosto((int) minutos, tarifaPorMinuto);
        
        // Aplicar descuento
        Double montoConDescuento = montoBase * (1 - (alquiler.getDescuentoAplicado() / 100));
        alquiler.setMontoTotal(montoConDescuento);
    }
    
    /**
     * Obtiene todos los alquileres de un usuario
     */
    public List<Alquiler> obtenerAlquileresPorUsuario(Long idUsuario) {
        return alquilerRepository.findByUsuarioId(idUsuario);
    }
    
    /**
     * Obtiene todos los alquileres activos
     */
    public List<Alquiler> obtenerAlquileresActivos() {
        return alquilerRepository.findByEstado(Alquiler.EstadoAlquiler.ACTIVO);
    }
}
