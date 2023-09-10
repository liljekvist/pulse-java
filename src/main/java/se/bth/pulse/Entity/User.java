package se.bth.pulse.Entity;

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
    private String email;

    private String password; //salted and hashed with scrypt

    private String firstname;

    private String lastname;

    private String phonenr;

    private Boolean enabled;

    @OneToOne
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
