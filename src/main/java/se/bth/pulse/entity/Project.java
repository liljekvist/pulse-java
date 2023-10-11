package se.bth.pulse.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.Date;
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
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Project {

  /**
   * Enum for the report interval.
   */
  public enum ReportInterval {
    DAILY,
    WEEKLY,
    BIWEEKLY,
    MONTHLY;

    public int getHours() {
      return switch (this) {
        case DAILY -> 24;
        case WEEKLY -> 24 * 7;
        case BIWEEKLY -> 24 * 7 * 2;
        case MONTHLY -> 24 * 7 * 4;
        default -> 0;
      };
    }
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String description;

  private ReportInterval reportInterval;

  private Date startDate;

  private Date endDate;

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

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public void setReports(List<Report> reports) {
    this.reports = reports;
  }
}
