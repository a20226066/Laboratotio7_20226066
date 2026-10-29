package pe.edu.pucp.gtics.doctormanager.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="full_name",nullable=false,length=100)
    private String fullName;
    @Column(name="dni",nullable=false,length=8,unique=true)
    private String dni;
    @Column(name="phone_number",nullable=false,length=9)
    private String phoneNumber;
    @Column(name="email",nullable=false,length=100)
    private String email;
    @Column(name="incorporation_date",nullable=false)
    private LocalDate incorporationDate;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="state_id",nullable=false)
    private DoctorState state;

    // Muchos doctores pueden compartir especialidad y cada doctor puede tener varias.
    // La tabla intermedia doctor_specialty es gestionada por JPA.
    @ManyToMany
    @JoinTable(name="doctor_specialty",
            joinColumns=@JoinColumn(name="doctor_id"),
            inverseJoinColumns=@JoinColumn(name="specialty_id"))
    private Set<Specialty> specialties=new LinkedHashSet<>();

    // Lado inverso de Clinic.doctors: la tabla dueña es Clinic.
    @ManyToMany(mappedBy="doctors")
    private Set<Clinic> clinics=new LinkedHashSet<>();

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
    public DoctorState getState(){return state;}
    public void setState(DoctorState state){this.state=state;}
    public Set<Specialty> getSpecialties(){return specialties;}
    public void setSpecialties(Set<Specialty> specialties){this.specialties=specialties;}
    public Set<Clinic> getClinics(){return clinics;}
    public void setClinics(Set<Clinic> clinics){this.clinics=clinics;}
}
