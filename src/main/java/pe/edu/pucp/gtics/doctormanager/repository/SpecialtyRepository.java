package pe.edu.pucp.gtics.doctormanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.gtics.doctormanager.entity.Specialty;

public interface SpecialtyRepository extends JpaRepository<Specialty,Integer>{
    List<Specialty> findAllByOrderByNameAsc();
}
