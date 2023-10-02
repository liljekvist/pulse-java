package se.bth.pulse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;


/**
 * This is the entity class for the table Setting.
 * This class models the table Settings meaning that each instance of this class represents
 * a row in the table.
 * This table is intended to hold settings for the application.
 * The settings are stored as key-value pairs.
 * Using this class the table is created.
 */
@Getter
@Entity(name = "Setting")
@Table(name = "Setting")
public class Setting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String value;


  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
