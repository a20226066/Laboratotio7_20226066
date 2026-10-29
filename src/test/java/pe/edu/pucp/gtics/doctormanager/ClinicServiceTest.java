package pe.edu.pucp.gtics.doctormanager;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import pe.edu.pucp.gtics.doctormanager.entity.Doctor;
import pe.edu.pucp.gtics.doctormanager.entity.Specialty;
import pe.edu.pucp.gtics.doctormanager.form.ClinicForm;
import pe.edu.pucp.gtics.doctormanager.repository.ClinicRepository;
import pe.edu.pucp.gtics.doctormanager.repository.DoctorRepository;
import pe.edu.pucp.gtics.doctormanager.service.ClinicService;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// P4: una clínica no puede asignar dos doctores con la misma especialidad.
class ClinicServiceTest {
    @Test
    void noPermiteDoctoresConLaMismaEspecialidad() {
        ClinicRepository clinics=mock(ClinicRepository.class);
        DoctorRepository doctors=mock(DoctorRepository.class);
        ClinicService service=new ClinicService(clinics,doctors);
        Specialty cardio=new Specialty();cardio.setId(1);cardio.setName("Cardiología");
        Doctor ana=new Doctor();ana.setId(10);ana.getSpecialties().add(cardio);
        Doctor pedro=new Doctor();pedro.setId(11);pedro.getSpecialties().add(cardio);
        when(doctors.findDetailById(10)).thenReturn(Optional.of(ana));
        when(doctors.findDetailById(11)).thenReturn(Optional.of(pedro));
        ClinicForm form=new ClinicForm();form.setDoctorIds(List.of(10,11));
        BindingResult result=new BeanPropertyBindingResult(form,"clinicForm");
        service.validarDoctores(form,result);
        assertTrue(result.hasGlobalErrors());
    }
}
