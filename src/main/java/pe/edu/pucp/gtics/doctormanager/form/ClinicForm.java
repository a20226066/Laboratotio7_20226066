package pe.edu.pucp.gtics.doctormanager.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import pe.edu.pucp.gtics.doctormanager.entity.Clinic;

// Clase para Data Binding: recoge los valores del formulario HTML.
// Separar el formulario de la entidad evita modificar directamente la BD
// con datos no validados ingresados por el usuario.
public class ClinicForm {
    private Integer id;

    @NotBlank(message = "El nombre de la clínica es obligatorio")
    @Size(min = 5, max = 100, message = "El nombre debe tener entre 5 y 100 caracteres")
    private String name;

    @NotBlank(message = "La dirección de la clínica es obligatoria")
    @Size(min = 10, max = 150, message = "La dirección debe tener entre 10 y 150 caracteres")
    private String address;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener exactamente 9 dígitos numéricos")
    private String phoneNumber;

    // Cargamos los datos de la BD al editar, reutilizando el mismo formulario.
    public static ClinicForm fromEntity(Clinic clinic) {
        ClinicForm form = new ClinicForm();
        form.id = clinic.getId();
        form.name = clinic.getName();
        form.address = clinic.getAddress();
        form.phoneNumber = clinic.getPhoneNumber();
        return form;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
