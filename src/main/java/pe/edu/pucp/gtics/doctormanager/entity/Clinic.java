package pe.edu.pucp.gtics.doctormanager.entity;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "clinic")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "address", nullable = false, length = 150)
    private String address;
    @Column(name = "phone_number", nullable = false, length = 9)
    private String phoneNumber;

    // Una clínica tiene varios doctores y un doctor trabaja en varias clínicas.
    // @JoinTable vincula esta relación con clinic_doctor del diagrama.
    @ManyToMany
    @JoinTable(name="clinic_doctor",
            joinColumns=@JoinColumn(name="clinic_id"),
            inverseJoinColumns=@JoinColumn(name="doctor_id"))
    private Set<Doctor> doctors=new LinkedHashSet<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public Set<Doctor> getDoctors(){return doctors;}
    public void setDoctors(Set<Doctor> doctors){this.doctors=doctors;}
}
