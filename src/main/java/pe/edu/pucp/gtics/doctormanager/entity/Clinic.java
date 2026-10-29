package pe.edu.pucp.gtics.doctormanager.entity;

import jakarta.persistence.*;

// Entidad JPA: cada objeto Clinic representa una fila de la tabla clinic.
@Entity
@Table(name = "clinic")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT en MySQL.
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 150)
    private String address;

    // String permite conservar un 0 inicial en un teléfono de 9 dígitos.
    @Column(name = "phone_number", nullable = false, length = 9)
    private String phoneNumber;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
