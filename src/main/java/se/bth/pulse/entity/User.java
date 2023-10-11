package se.bth.pulse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

/**
 * This is the entity class for the table User.
 * This class models the table Users meaning that each instance of this class represents
 * a row in the table.
 * Using this class the table is created.
 * This class also serves as the username and password holder for our Custom UserDetailsService.
 */
@Getter
@Entity(name = "User")
@Table(name = "User")
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

  @ColumnDefault("false")
  private Boolean credentialsExpired = false;

  @Getter
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

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public void setProjects(Set<Project> projects) {
    this.projects = projects;
  }

  public void setCredentialsExpired(Boolean credentialsExpired) {
    this.credentialsExpired = credentialsExpired;
  }
}
