package se.bth.pulse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity(name = "Report")
@Table(name = "Report")
public class Report {

  public enum Status {
    PENDING,
    APPROVED,
    REJECTED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String date;

  private String content;

  private Status status;

  @ManyToOne(fetch = FetchType.LAZY)
  private Project project;

  public void setDate(String date) {
    this.date = date;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
