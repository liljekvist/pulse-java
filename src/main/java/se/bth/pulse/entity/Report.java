package se.bth.pulse.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import org.hibernate.annotations.Nationalized;

@Getter
@Entity(name = "Report")
@Table(name = "Report")
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Report {

  public enum Status {
    MISSING,
    SUBMITTED,
    READ
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Date dueDate;

  private String name;

  @Nationalized
  @Lob
  @Column(columnDefinition = "LONGTEXT")
  private String content;

  private Status status;

  @OneToMany(mappedBy = "report", fetch = FetchType.LAZY)
  private List<ReportComment> comments;

  @ManyToOne
  private Project project;

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public void addComment(ReportComment comment) {
    this.comments.add(comment);
  }
}
