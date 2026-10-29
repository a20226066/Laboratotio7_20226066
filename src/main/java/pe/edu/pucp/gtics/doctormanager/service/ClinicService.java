package pe.edu.pucp.gtics.doctormanager.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import pe.edu.pucp.gtics.doctormanager.entity.Clinic;
import pe.edu.pucp.gtics.doctormanager.entity.Doctor;
import pe.edu.pucp.gtics.doctormanager.entity.Specialty;
import pe.edu.pucp.gtics.doctormanager.form.ClinicForm;
import pe.edu.pucp.gtics.doctormanager.repository.ClinicRepository;
import pe.edu.pucp.gtics.doctormanager.repository.DoctorRepository;

@Service
public class ClinicService {
    private final ClinicRepository clinics;
    private final DoctorRepository doctors;
    public ClinicService(ClinicRepository clinics,DoctorRepository doctors){this.clinics=clinics;this.doctors=doctors;}

    // P4: Regla de negocio: dos doctores de la MISMA clínica no deben compartir
    // ni una sola especialidad. Si cada médico tiene dos especialidades, se revisan ambas.
    public void validarDoctores(ClinicForm form,BindingResult errors){
        if(form.getDoctorIds()==null) return; // Ningún doctor es válido: no es obligatorio asignarlos.
        Set<Integer> ids=new HashSet<>();
        Set<Integer> especialidadesUsadas=new HashSet<>();
        for(Integer id:form.getDoctorIds()){
            if(id==null || !ids.add(id)){
                errors.reject("clinic.doctoresDuplicados","No repita el mismo doctor en la clínica");
                continue;
            }
            Doctor doctor=doctors.findDetailById(id).orElse(null);
            if(doctor==null){
                errors.reject("clinic.doctorInexistente","Hay un doctor no registrado en la selección");
                continue;
            }
            for(Specialty sp:doctor.getSpecialties()){
                if(!especialidadesUsadas.add(sp.getId())){
                    errors.reject("clinic.especialidadDuplicada",
                        "No se permiten dos doctores con la especialidad " + sp.getName() + " en la misma clínica");
                }
            }
        }
    }

    @Transactional
    public void guardar(ClinicForm form){
        Clinic clinic=form.getId()==null?new Clinic():clinics.findById(form.getId())
                .orElseThrow(()->new IllegalArgumentException("La clínica no existe"));
        clinic.setName(form.getName().trim());
        clinic.setAddress(form.getAddress().trim());
        clinic.setPhoneNumber(form.getPhoneNumber());
        // Conservar la entidad administrada y modificar la colección JPA, no crear otra fila.
        clinic.getDoctors().clear();
        if(form.getDoctorIds()!=null){
            for(Integer id:form.getDoctorIds()){
                Doctor doctor=doctors.findById(id).orElseThrow(
                    ()->new IllegalArgumentException("Doctor no existente"));
                clinic.getDoctors().add(doctor);
            }
        }
        clinics.save(clinic); // Guarda clinic y su tabla intermedia clinic_doctor.
    }

    @Transactional
    public void eliminar(Integer id){
        Clinic clinic=clinics.findById(id).orElseThrow(()->new IllegalArgumentException("No existe"));
        clinic.getDoctors().clear(); // Quita relaciones antes de borrar la clínica.
        clinics.delete(clinic);
    }
}
