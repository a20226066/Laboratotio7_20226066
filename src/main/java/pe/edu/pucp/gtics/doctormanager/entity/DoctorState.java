package pe.edu.pucp.gtics.doctormanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name="doctor_state")
public class DoctorState {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name", nullable=false, length=20)
    private String name;
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id=id; }
    public String getName() { return name; }
    public void setName(String name) { this.name=name; }
}
