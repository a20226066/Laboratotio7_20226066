package pe.edu.pucp.gtics.doctormanager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.gtics.doctormanager.entity.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic,Integer>{
    List<Clinic> findAllByOrderByNameAsc();

    // Consulta la clínica y sus doctores/especialidades antes de abrir la vista detalle.
    @EntityGraph(attributePaths={"doctors","doctors.specialties"})
    @Query("SELECT c FROM Clinic c WHERE c.id = :id")
    Optional<Clinic> findDetailById(@Param("id") Integer id);

    // Más adelante se utiliza al editar especialidades de un doctor.
    List<Clinic> findDistinctByDoctors_Id(Integer doctorId);
}
