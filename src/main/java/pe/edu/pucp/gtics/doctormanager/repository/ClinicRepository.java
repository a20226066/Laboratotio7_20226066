package pe.edu.pucp.gtics.doctormanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.gtics.doctormanager.entity.Clinic;
import java.util.List;

// Spring Data JPA crea automáticamente findById, findAll, save y deleteById.
public interface ClinicRepository extends JpaRepository<Clinic, Integer> {
    // Query Method: el nombre define la consulta que Spring genera por nosotros.
    List<Clinic> findAllByOrderByNameAsc();
}
