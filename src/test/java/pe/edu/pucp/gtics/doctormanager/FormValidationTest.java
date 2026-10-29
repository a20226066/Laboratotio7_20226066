package pe.edu.pucp.gtics.doctormanager;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import pe.edu.pucp.gtics.doctormanager.form.ClinicForm;
import pe.edu.pucp.gtics.doctormanager.form.DoctorForm;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// Pruebas de las anotaciones @NotBlank, @Pattern, @PastOrPresent, etc.
// No necesitan conexión a la base de datos.
class FormValidationTest {
    private final Validator validator=Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void clinicaValida() {
        ClinicForm form=new ClinicForm();
        form.setName("Clínica del Norte");form.setAddress("Av. Siempre Viva 123");form.setPhoneNumber("987654321");
        assertTrue(validator.validate(form).isEmpty());
    }

    @Test
    void clinicaRechazaTelefonoConLetras() {
        ClinicForm form=new ClinicForm();
        form.setName("Clínica del Norte");form.setAddress("Av. Siempre Viva 123");form.setPhoneNumber("abc123456");
        assertTrue(validator.validate(form).stream().anyMatch(e->e.getPropertyPath().toString().equals("phoneNumber")));
    }

    @Test
    void doctorRechazaFechaFutura() {
        DoctorForm form=new DoctorForm();
        form.setFullName("Carlos Pérez");form.setDni("12345678");form.setPhoneNumber("987654321");
        form.setEmail("carlos@example.com");form.setIncorporationDate(LocalDate.now().plusDays(1));
        form.setStateId(1);form.setSpecialtyIds(List.of(1));
        assertTrue(validator.validate(form).stream().anyMatch(e->e.getPropertyPath().toString().equals("incorporationDate")));
    }

    @Test
    void doctorRequiereAlMenosUnaEspecialidad() {
        DoctorForm form=new DoctorForm();
        form.setFullName("Carlos Pérez");form.setDni("12345678");form.setPhoneNumber("987654321");
        form.setEmail("carlos@example.com");form.setIncorporationDate(LocalDate.now());form.setStateId(1);
        assertTrue(validator.validate(form).stream().anyMatch(e->e.getPropertyPath().toString().equals("specialtyIds")));
    }
}
