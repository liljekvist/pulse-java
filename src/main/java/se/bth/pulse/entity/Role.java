package se.bth.pulse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

/**
 * This is the entity class for the table Role.
 * This class models the table Roles meaning that each instance of this class represents
 * a row in the table.
 * Using this class the table is created.
 * This class is used to hold the roles that can be connected to the user.
 * The connection between user and role is a one-to-many relationship.
 * This means that one role can be connected to many users but one user can only be connected
 * to one role.
 */
@Getter
@Entity(name = "Role")
@Table(name = "role")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  //site wide and structured like this "rwx" where r is read, w is write and x is full access
  private String premissions;

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPremissions(String premissions) {
    this.premissions = premissions;
  }
}
