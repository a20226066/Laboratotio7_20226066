package pe.edu.pucp.gtics.doctormanager.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.pucp.gtics.doctormanager.entity.Doctor;
import pe.edu.pucp.gtics.doctormanager.entity.DoctorState;
import pe.edu.pucp.gtics.doctormanager.form.DoctorForm;
import pe.edu.pucp.gtics.doctormanager.repository.DoctorRepository;
import pe.edu.pucp.gtics.doctormanager.repository.DoctorStateRepository;

@Controller
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorRepository doctorRepository;
    private final DoctorStateRepository stateRepository;
    public DoctorController(DoctorRepository doctorRepository,DoctorStateRepository stateRepository){
        this.doctorRepository=doctorRepository;
        this.stateRepository=stateRepository;
    }
    // @ModelAttribute a nivel de método: disponible para crear, editar y errores.
    @ModelAttribute("states")
    public List<DoctorState> estados(){return stateRepository.findAllByOrderByNameAsc();}

    @GetMapping({"","/list"})
    public String listar(Model model){
        model.addAttribute("doctors",doctorRepository.findAllWithState());
        return "doctors/list";
    }
    @GetMapping("/new")
    public String nuevo(@ModelAttribute("doctorForm") DoctorForm form){return "doctors/form";}

    @GetMapping("/edit")
    public String editar(@RequestParam Integer id,Model model,RedirectAttributes flash){
        return doctorRepository.findById(id).map(doctor->{
            model.addAttribute("doctorForm",DoctorForm.fromEntity(doctor));
            return "doctors/form";
        }).orElseGet(()->{
            flash.addFlashAttribute("error","El doctor solicitado no existe");
            return "redirect:/doctors";
        });
    }
    @PostMapping("/save")
    public String guardar(@Valid @ModelAttribute("doctorForm") DoctorForm form,
                          BindingResult errors, RedirectAttributes flash){
        // Validación personalizada: el estado debe existir en la base de datos.
        DoctorState state=null;
        if(form.getStateId()!=null){
            state=stateRepository.findById(form.getStateId()).orElse(null);
            if(state==null){errors.rejectValue("stateId","estado.invalido","Seleccione un estado válido");}
        }
        // Un DNI identifica a una persona: evitamos duplicarlo.
        if(form.getDni()!=null && form.getDni().matches("\\d{8}")){
            doctorRepository.findByDni(form.getDni()).ifPresent(existing->{
                if(!existing.getId().equals(form.getId())){
                    errors.rejectValue("dni","dni.duplicado","El DNI ya está registrado");
                }
            });
        }
        if(errors.hasErrors()) return "doctors/form";
        Doctor doctor;
        if(form.getId()==null) doctor=new Doctor();
        else {
            doctor=doctorRepository.findById(form.getId()).orElse(null);
            if(doctor==null){
                errors.reject("doctor.noexiste","No se encontró el doctor para editar");
                return "doctors/form";
            }
        }
        doctor.setFullName(form.getFullName().trim());
        doctor.setDni(form.getDni());
        doctor.setPhoneNumber(form.getPhoneNumber());
        doctor.setEmail(form.getEmail().trim());
        doctor.setIncorporationDate(form.getIncorporationDate());
        doctor.setState(state);
        try{
            doctorRepository.save(doctor);
        }catch(DataIntegrityViolationException ex){
            errors.reject("doctor.integridad","No se pudo guardar el doctor; compruebe los datos");
            return "doctors/form";
        }
        flash.addFlashAttribute("msg","Doctor guardado correctamente");
        return "redirect:/doctors";
    }
    @PostMapping("/delete")
    public String eliminar(@RequestParam Integer id,RedirectAttributes flash){
        if(!doctorRepository.existsById(id)){
            flash.addFlashAttribute("error","El doctor no existe");
        }else{
            try{
                doctorRepository.deleteById(id);
                flash.addFlashAttribute("msg","Doctor eliminado correctamente");
            }catch(DataIntegrityViolationException ex){
                flash.addFlashAttribute("error","No se pudo eliminar el doctor por relaciones existentes");
            }
        }
        return "redirect:/doctors";
    }
}
