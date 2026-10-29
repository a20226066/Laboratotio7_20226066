package pe.edu.pucp.gtics.doctormanager.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.pucp.gtics.doctormanager.entity.Clinic;
import pe.edu.pucp.gtics.doctormanager.form.ClinicForm;
import pe.edu.pucp.gtics.doctormanager.repository.ClinicRepository;

@Controller
@RequestMapping("/clinics") // Las rutas se agrupan: /clinics, /clinics/new, etc.
public class ClinicController {
    private final ClinicRepository clinicRepository;
    public ClinicController(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository; // Inyección por constructor (clase JPA).
    }

    @GetMapping({"", "/list"})
    public String listar(Model model) {
        model.addAttribute("clinics", clinicRepository.findAllByOrderByNameAsc());
        return "clinics/list";
    }

    @GetMapping("/new")
    public String nuevo(@ModelAttribute("clinicForm") ClinicForm form) {
        // El objeto vacío llega al formulario para que th:object lo utilice.
        return "clinics/form";
    }

    @GetMapping("/edit")
    public String editar(@RequestParam Integer id, Model model, RedirectAttributes flash) {
        return clinicRepository.findById(id).map(clinic -> {
            model.addAttribute("clinicForm", ClinicForm.fromEntity(clinic));
            return "clinics/form"; // MISMA vista que crear.
        }).orElseGet(() -> {
            flash.addFlashAttribute("error", "La clínica no existe");
            return "redirect:/clinics";
        });
    }

    @PostMapping("/save")
    public String guardar(@Valid @ModelAttribute("clinicForm") ClinicForm form,
                          BindingResult errors, RedirectAttributes flash) {
        // BindingResult siempre va inmediatamente después de @Valid.
        if (errors.hasErrors()) { return "clinics/form"; }
        Clinic clinic;
        if (form.getId() == null) {
            clinic = new Clinic();
        } else {
            clinic = clinicRepository.findById(form.getId()).orElse(null);
            if (clinic == null) {
                errors.reject("clinic.noexiste", "No se encontró la clínica para editar");
                return "clinics/form";
            }
        }
        clinic.setName(form.getName().trim());
        clinic.setAddress(form.getAddress().trim());
        clinic.setPhoneNumber(form.getPhoneNumber());
        clinicRepository.save(clinic); // INSERT o UPDATE, según su ID.
        flash.addFlashAttribute("msg", "Clínica guardada correctamente");
        return "redirect:/clinics"; // PRG: POST -> redirect -> GET.
    }

    @PostMapping("/delete")
    public String eliminar(@RequestParam Integer id, RedirectAttributes flash) {
        if (clinicRepository.existsById(id)) {
            try {
                clinicRepository.deleteById(id);
                flash.addFlashAttribute("msg", "Clínica eliminada correctamente");
            } catch (DataIntegrityViolationException ex) {
                flash.addFlashAttribute("error", "No se pudo eliminar: la clínica tiene registros relacionados");
            }
        } else {
            flash.addFlashAttribute("error", "La clínica no existe");
        }
        return "redirect:/clinics";
    }
}
