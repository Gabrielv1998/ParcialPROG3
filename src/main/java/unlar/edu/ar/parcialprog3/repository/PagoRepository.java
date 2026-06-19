package unlar.edu.ar.parcialprog3.repository;

import unlar.edu.ar.parcialprog3.domain.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByAlquilerId(Long alquilerId);
    List<Pago> findByEstado(Pago.EstadoPago estado);
    List<Pago> findByMetodoPago(Pago.MetodoPago metodoPago);
}
