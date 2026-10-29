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
import pe.edu.pucp.gtics.doctormanager.form.ClinicForm;
import pe.edu.pucp.gtics.doctormanager.repository.ClinicRepository;
import pe.edu.pucp.gtics.doctormanager.repository.DoctorRepository;
import pe.edu.pucp.gtics.doctormanager.service.ClinicService;

@Controller
@RequestMapping("/clinics")
public class ClinicController {
    private final ClinicRepository clinics;
    private final DoctorRepository doctors;
    private final ClinicService service;
    public ClinicController(ClinicRepository clinics,DoctorRepository doctors,ClinicService service){
        this.clinics=clinics;this.doctors=doctors;this.service=service;
    }

    // Disponible tanto en GET como en POST cuando se vuelve al formulario con errores.
    @ModelAttribute("availableDoctors")
    public List<Doctor> doctoresDisponibles(){return doctors.findAllWithSpecialties();}

    @GetMapping({"","/list"})
    public String listar(Model model){
        model.addAttribute("clinics",clinics.findAllByOrderByNameAsc());
        return "clinics/list";
    }
    @GetMapping("/new")
    public String nuevo(@ModelAttribute("clinicForm") ClinicForm form){return "clinics/form";}

    @GetMapping("/edit")
    public String editar(@RequestParam Integer id,Model model,RedirectAttributes flash){
        return clinics.findDetailById(id).map(clinic->{
            model.addAttribute("clinicForm",ClinicForm.fromEntity(clinic));
            return "clinics/form";
        }).orElseGet(()->{
            flash.addFlashAttribute("error","La clínica no existe");
            return "redirect:/clinics";
        });
    }

    @PostMapping("/save")
    public String guardar(@Valid @ModelAttribute("clinicForm") ClinicForm form,
                          BindingResult errors,RedirectAttributes flash){
        // Primero @Valid, luego las reglas de negocio adicionales del laboratorio.
        if(!errors.hasErrors())service.validarDoctores(form,errors);
        if(errors.hasErrors()) return "clinics/form";
        if(form.getId()!=null && !clinics.existsById(form.getId())){
            errors.reject("clinic.noexiste","La clínica ya no existe");
            return "clinics/form";
        }
        try{
            service.guardar(form);
        }catch(DataIntegrityViolationException|IllegalArgumentException ex){
            errors.reject("clinic.integridad","No se pudo guardar; revise la selección de doctores");
            return "clinics/form";
        }
        flash.addFlashAttribute("msg","Clínica y médicos asignados guardados correctamente");
        return "redirect:/clinics";
    }

    @GetMapping("/detail")
    public String detalle(@RequestParam Integer id,Model model,RedirectAttributes flash){
        return clinics.findDetailById(id).map(clinic->{
            model.addAttribute("clinic",clinic);
            return "clinics/detail";
        }).orElseGet(()->{
            flash.addFlashAttribute("error","La clínica no existe");
            return "redirect:/clinics";
        });
    }

    @PostMapping("/delete")
    public String eliminar(@RequestParam Integer id,RedirectAttributes flash){
        if(!clinics.existsById(id)){
            flash.addFlashAttribute("error","La clínica no existe");
        }else{
            try{
                service.eliminar(id);
                flash.addFlashAttribute("msg","Clínica eliminada correctamente");
            }catch(DataIntegrityViolationException ex){
                flash.addFlashAttribute("error","No se pudo eliminar la clínica debido a relaciones existentes");
            }
        }
        return "redirect:/clinics";
    }
}
