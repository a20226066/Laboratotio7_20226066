package pe.edu.pucp.gtics.doctormanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.gtics.doctormanager.entity.DoctorState;
import java.util.List;

public interface DoctorStateRepository extends JpaRepository<DoctorState,Integer>{
    List<DoctorState> findAllByOrderByNameAsc();
}
