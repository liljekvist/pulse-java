package se.bth.pulse.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

  public enum ReportInterval {
    DAILY,
    WEEKLY,
    BIWEEKLY,
    MONTHLY
  }

  public enum WeekDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String description;

  private ReportInterval reportInterval;

  private WeekDay reportDay;

  @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
  private List<Report> reports;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<User> users;

  public void setId(Integer id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public void setReportInterval(ReportInterval reportInterval) {
    this.reportInterval = reportInterval;
  }

  public void setReportDay(WeekDay reportDay) {
    this.reportDay = reportDay;
  }
}
