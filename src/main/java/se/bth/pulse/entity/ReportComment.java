package se.bth.pulse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;

@Getter
@Entity(name = "ReportComment")
@Table(name = "report_comment")
public class ReportComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String content;

  @ManyToOne
  private Report report;

  private Date date;

  public void setContent(String content) {
    this.content = content;
  }

  public void setReport(Report report) {
    this.report = report;
  }

  public void setDate(Date date) {
    this.date = date;
  }
}
