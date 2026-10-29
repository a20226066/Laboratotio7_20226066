package pe.edu.pucp.gtics.doctormanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.pucp.gtics.doctormanager.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor,Integer>{
    // JOIN FETCH carga el estado junto al doctor (sin acceder a relaciones fuera de transacción).
    @Query("SELECT d FROM Doctor d JOIN FETCH d.state ORDER BY d.fullName")
    List<Doctor> findAllWithState();
    Optional<Doctor> findByDni(String dni);
}
