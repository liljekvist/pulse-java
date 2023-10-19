package se.bth.pulse.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;

/**
 * This is the entity class for the table Authority.
 * It is used to hold the authorities that can be connected to the role.
 */
@Getter
@Entity(name = "Authority")
@Table(name = "authority")
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Authority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "authorities", cascade = CascadeType.ALL)
  List<Role> roles;

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }
}
