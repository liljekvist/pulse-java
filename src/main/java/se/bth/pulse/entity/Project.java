package se.bth.pulse.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;

/**
 * This is the entity class for the table Project.
 * This class models the table Projects meaning that each instance of this class represents
 * a row in the table.
 * Using this class the table is created.
 * This class is used to hold the projects that can be connected to the user.
 * The connection between user and project is a many-to-many relationship.
 * This means that one project can be connected to many users and one user can be connected
 * to many projects.
 */
@Getter
@Entity(name = "Project")
@Table(name = "Project")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String description;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<User> users;

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }
}
