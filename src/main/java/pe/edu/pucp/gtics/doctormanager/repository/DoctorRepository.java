package pe.edu.pucp.gtics.doctormanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.gtics.doctormanager.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor,Integer>{
    @Query("SELECT d FROM Doctor d JOIN FETCH d.state ORDER BY d.fullName")
    List<Doctor> findAllWithState();

    // El checkbox de asignación de clínicas también necesita las especialidades.
    @Query("SELECT DISTINCT d FROM Doctor d LEFT JOIN FETCH d.specialties ORDER BY d.fullName")
    List<Doctor> findAllWithSpecialties();

    @EntityGraph(attributePaths={"specialties","state"})
    @Query("SELECT d FROM Doctor d WHERE d.id = :id")
    Optional<Doctor> findDetailById(@Param("id") Integer id);

    Optional<Doctor> findByDni(String dni);
}
