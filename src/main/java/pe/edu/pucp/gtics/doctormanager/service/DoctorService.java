package pe.edu.pucp.gtics.doctormanager.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import pe.edu.pucp.gtics.doctormanager.entity.*;
import pe.edu.pucp.gtics.doctormanager.form.DoctorForm;
import pe.edu.pucp.gtics.doctormanager.repository.*;

@Service
public class DoctorService {
    private final DoctorRepository doctors;
    private final ClinicRepository clinics;
    private final SpecialtyRepository specialties;
    public DoctorService(DoctorRepository doctors,ClinicRepository clinics,SpecialtyRepository specialties){
        this.doctors=doctors;this.clinics=clinics;this.specialties=specialties;
    }

    public void validarEspecialidades(DoctorForm form,BindingResult errors){
        if(form.getSpecialtyIds()==null || form.getSpecialtyIds().isEmpty()) return;
        Set<Integer> ids=new HashSet<>();
        for(Integer id:form.getSpecialtyIds()){
            if(id==null || !ids.add(id)){
                errors.rejectValue("specialtyIds","especialidad.duplicada","No se puede repetir una especialidad");
            }else if(!specialties.existsById(id)){
                errors.rejectValue("specialtyIds","especialidad.invalida","La especialidad seleccionada no existe");
            }
        }
        if(form.getId()==null) return; // Doctor nuevo: todavía no trabaja en clínicas.
        // Evita que al editar un doctor se rompa la regla que ya se verificó al asignarlo.
        for(Clinic clinic:clinics.findDistinctByDoctors_Id(form.getId())){
            Clinic detallada=clinics.findDetailById(clinic.getId()).orElse(null);
            if(detallada==null) continue;
            for(Doctor otro:detallada.getDoctors()){
                if(otro.getId().equals(form.getId())) continue; // No compararse consigo mismo.
                for(Specialty sp:otro.getSpecialties()){
                    if(ids.contains(sp.getId())){
                        errors.reject("clinic.especialidadDuplicada",
                            "No se puede asignar " + sp.getName() + ": en " + detallada.getName()
                            + " ya trabaja otro doctor con esa especialidad");
                    }
                }
            }
        }
    }

    @Transactional
    public void guardar(DoctorForm form,DoctorState state){
        Doctor doctor=form.getId()==null?new Doctor():doctors.findById(form.getId())
                .orElseThrow(()->new IllegalArgumentException("Doctor no encontrado"));
        doctor.setFullName(form.getFullName().trim());
        doctor.setDni(form.getDni());doctor.setPhoneNumber(form.getPhoneNumber());
        doctor.setEmail(form.getEmail().trim());
        doctor.setIncorporationDate(form.getIncorporationDate());doctor.setState(state);
        // P5: el lado propietario Doctor controla doctor_specialty.
        doctor.getSpecialties().clear();
        for(Integer id:form.getSpecialtyIds()){
            Specialty sp=specialties.findById(id).orElseThrow(
                ()->new IllegalArgumentException("Especialidad inexistente"));
            doctor.getSpecialties().add(sp);
        }
        doctors.save(doctor);
    }

    @Transactional
    public void eliminar(Integer id){
        Doctor doctor=doctors.findById(id).orElseThrow(()->new IllegalArgumentException("Doctor no existe"));
        // La clínica es propietaria de clinic_doctor, por eso borramos las parejas ahí.
        for(Clinic clinic:clinics.findDistinctByDoctors_Id(id)){
            clinic.getDoctors().remove(doctor);
        }
        doctor.getSpecialties().clear();
        doctors.delete(doctor);
    }
}
