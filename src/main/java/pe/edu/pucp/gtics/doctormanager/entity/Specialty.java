package pe.edu.pucp.gtics.doctormanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name="specialty")
public class Specialty {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name",nullable=false,length=70)
    private String name;
    @Column(name="code",nullable=false,length=6,columnDefinition="CHAR(6)")
    private String code;
    public Integer getId(){return id;}
    public void setId(Integer id){this.id=id;}
    public String getName(){return name;}
    public void setName(String name){this.name=name;}
    public String getCode(){return code;}
    public void setCode(String code){this.code=code;}
}
