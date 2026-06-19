package unlar.edu.ar.parcialprog3.repository;

import unlar.edu.ar.parcialprog3.domain.Alquiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
    List<Alquiler> findByUsuarioId(Long usuarioId);
    List<Alquiler> findByVehiculoId(Long vehiculoId);
    List<Alquiler> findByEstado(Alquiler.EstadoAlquiler estado);
}
