package se.bth.pulse.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity(name = "User")
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "firstname")
    private String firstname;

    @CsvBindByName(column = "lastname")
    private String lastname;

    @CsvBindByName(column = "phonenr")
    private String phonenr;


    private String password; //salted and hashed with bcrypt

    private Boolean enabled;

    @ManyToOne
    private Role role;

    @ManyToMany(mappedBy = "users")
    private Set<Project> projects;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhonenr(String phonenr) {
        this.phonenr = phonenr;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }


    public String getRole() {
        return role.getName();
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
