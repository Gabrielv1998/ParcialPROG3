package unlar.edu.ar.parcialprog3.repository;

import unlar.edu.ar.parcialprog3.domain.EstacionAnclaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstacionAnclajRepository extends JpaRepository<EstacionAnclaje, Long> {
    Optional<EstacionAnclaje> findByNombre(String nombre);
    List<EstacionAnclaje> findByActiva(Boolean activa);
}
