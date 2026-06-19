package unlar.edu.ar.parcialprog3.repository;

import unlar.edu.ar.parcialprog3.domain.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPatente(String patente);
    List<Vehiculo> findByTipo(Vehiculo.TipoVehiculo tipo);
    List<Vehiculo> findByEstadoCicloAndNivelBateriaGreaterThanEqual(
            Vehiculo.EstadoCicloVida estadoCiclo, Integer nivelBateria);
    List<Vehiculo> findByEstacionActualId(Long estacionId);
}
