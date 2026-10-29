package pe.edu.pucp.gtics.doctormanager.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.pucp.gtics.doctormanager.entity.DoctorState;
import pe.edu.pucp.gtics.doctormanager.entity.Specialty;
import pe.edu.pucp.gtics.doctormanager.form.DoctorForm;
import pe.edu.pucp.gtics.doctormanager.repository.*;
import pe.edu.pucp.gtics.doctormanager.service.DoctorService;

@Controller
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorRepository doctors;
    private final DoctorStateRepository states;
    private final SpecialtyRepository specialties;
    private final DoctorService service;
    public DoctorController(DoctorRepository doctors,DoctorStateRepository states,
                            SpecialtyRepository specialties,DoctorService service){
        this.doctors=doctors;this.states=states;this.specialties=specialties;this.service=service;
    }

    // Los dos catálogos quedan disponibles también cuando el POST tiene errores.
    @ModelAttribute("states")
    public List<DoctorState> estados(){return states.findAllByOrderByNameAsc();}
    @ModelAttribute("availableSpecialties")
    public List<Specialty> especialidades(){return specialties.findAllByOrderByNameAsc();}

    @GetMapping({"","/list"})
    public String listar(Model model){
        model.addAttribute("doctors",doctors.findAllWithState());
        return "doctors/list";
    }
    @GetMapping("/new")
    public String nuevo(@ModelAttribute("doctorForm") DoctorForm form){return "doctors/form";}

    @GetMapping("/edit")
    public String editar(@RequestParam Integer id,Model model,RedirectAttributes flash){
        return doctors.findDetailById(id).map(doctor->{
            model.addAttribute("doctorForm",DoctorForm.fromEntity(doctor));
            return "doctors/form"; // Único formulario para crear y editar.
        }).orElseGet(()->{
            flash.addFlashAttribute("error","El doctor solicitado no existe");
            return "redirect:/doctors";
        });
    }

    @PostMapping("/save")
    public String guardar(@Valid @ModelAttribute("doctorForm") DoctorForm form,
                          BindingResult errors,RedirectAttributes flash){
        // Las validaciones de anotaciones se ejecutan con @Valid.
        DoctorState state=null;
        if(form.getStateId()!=null){
            state=states.findById(form.getStateId()).orElse(null);
            if(state==null) errors.rejectValue("stateId","estado.invalido","Seleccione un estado válido");
        }
        // Comprobar que no haya dos doctores con el mismo DNI.
        if(form.getDni()!=null && form.getDni().matches("\\d{8}")){
            doctors.findByDni(form.getDni()).ifPresent(existing->{
                if(!existing.getId().equals(form.getId())){
                    errors.rejectValue("dni","dni.duplicado","El DNI ya está registrado");
                }
            });
        }
        // P5: se comprueban las especialidades y su compatibilidad con las clínicas actuales.
        if(!errors.hasFieldErrors("specialtyIds")) service.validarEspecialidades(form,errors);
        if(errors.hasErrors()) return "doctors/form";
        if(form.getId()!=null && !doctors.existsById(form.getId())){
            errors.reject("doctor.noexiste","El doctor ya no existe");
            return "doctors/form";
        }
        try{
            service.guardar(form,state);
        }catch(DataIntegrityViolationException|IllegalArgumentException ex){
            errors.reject("doctor.integridad","No se pudo guardar el doctor; revise los datos");
            return "doctors/form";
        }
        flash.addFlashAttribute("msg","Doctor y especialidades guardados correctamente");
        return "redirect:/doctors";
    }

    @GetMapping("/detail")
    public String detalle(@RequestParam Integer id,Model model,RedirectAttributes flash){
        return doctors.findDetailById(id).map(doctor->{
            model.addAttribute("doctor",doctor);
            return "doctors/detail";
        }).orElseGet(()->{
            flash.addFlashAttribute("error","El doctor no existe");
            return "redirect:/doctors";
        });
    }
    @PostMapping("/delete")
    public String eliminar(@RequestParam Integer id,RedirectAttributes flash){
        if(!doctors.existsById(id)){
            flash.addFlashAttribute("error","El doctor no existe");
        }else{
            try{
                service.eliminar(id);
                flash.addFlashAttribute("msg","Doctor eliminado correctamente");
            }catch(DataIntegrityViolationException ex){
                flash.addFlashAttribute("error","No se pudo eliminar el doctor por relaciones existentes");
            }
        }
        return "redirect:/doctors";
    }
}
