package pe.edu.pucp.gtics.doctormanager.form;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pe.edu.pucp.gtics.doctormanager.entity.Doctor;
import pe.edu.pucp.gtics.doctormanager.entity.Specialty;

// Objeto separado para recibir los datos del HTML mediante Data Binding.
public class DoctorForm {
    private Integer id;
    @NotBlank(message="El nombre completo es obligatorio")
    @Size(min=5,max=100,message="El nombre debe tener entre 5 y 100 caracteres")
    private String fullName;
    @NotBlank(message="El DNI es obligatorio")
    @Pattern(regexp="\\d{8}",message="El DNI debe contener exactamente 8 dígitos numéricos")
    private String dni;
    @NotBlank(message="El teléfono es obligatorio")
    @Pattern(regexp="\\d{9}",message="El teléfono debe contener exactamente 9 dígitos numéricos")
    private String phoneNumber;
    @NotBlank(message="El correo electrónico es obligatorio")
    @Email(message="Ingrese un correo electrónico válido")
    @Size(max=100,message="El correo no debe exceder 100 caracteres")
    private String email;
    @NotNull(message="La fecha de incorporación es obligatoria")
    @PastOrPresent(message="La fecha de incorporación no puede ser futura")
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate incorporationDate;
    @NotNull(message="Seleccione un estado")
    private Integer stateId;
    // P5: un doctor puede tener varias especialidades (lista de ID de los checkbox).
    @NotEmpty(message="Seleccione al menos una especialidad")
    private List<Integer> specialtyIds=new ArrayList<>();

    public static DoctorForm fromEntity(Doctor doctor){
        DoctorForm form=new DoctorForm();
        form.id=doctor.getId();form.fullName=doctor.getFullName();
        form.dni=doctor.getDni();form.phoneNumber=doctor.getPhoneNumber();
        form.email=doctor.getEmail();form.incorporationDate=doctor.getIncorporationDate();
        form.stateId=doctor.getState().getId();
        for(Specialty sp:doctor.getSpecialties()) form.specialtyIds.add(sp.getId());
        return form;
    }
    public Integer getId(){return id;}
    public void setId(Integer id){this.id=id;}
    public String getFullName(){return fullName;}
    public void setFullName(String fullName){this.fullName=fullName;}
    public String getDni(){return dni;}
    public void setDni(String dni){this.dni=dni;}
    public String getPhoneNumber(){return phoneNumber;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber=phoneNumber;}
    public String getEmail(){return email;}
    public void setEmail(String email){this.email=email;}
    public LocalDate getIncorporationDate(){return incorporationDate;}
    public void setIncorporationDate(LocalDate incorporationDate){this.incorporationDate=incorporationDate;}
    public Integer getStateId(){return stateId;}
    public void setStateId(Integer stateId){this.stateId=stateId;}
    public List<Integer> getSpecialtyIds(){return specialtyIds;}
    public void setSpecialtyIds(List<Integer> specialtyIds){this.specialtyIds=specialtyIds;}
}
