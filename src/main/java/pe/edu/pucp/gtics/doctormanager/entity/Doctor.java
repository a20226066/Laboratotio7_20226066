package pe.edu.pucp.gtics.doctormanager.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="full_name",nullable=false,length=100)
    private String fullName;
    // DNI y telefono son String porque son identificadores, NO cantidades.
    @Column(name="dni",nullable=false,length=8,unique=true)
    private String dni;
    @Column(name="phone_number",nullable=false,length=9)
    private String phoneNumber;
    @Column(name="email",nullable=false,length=100)
    private String email;
    @Column(name="incorporation_date",nullable=false)
    private LocalDate incorporationDate;
    // Muchos doctores pueden tener el mismo estado (Activo, Inactivo, etc.).
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="state_id",nullable=false)
    private DoctorState state;
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
}
