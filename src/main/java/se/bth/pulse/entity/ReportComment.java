package se.bth.pulse.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.Getter;
import org.hibernate.annotations.Nationalized;

@Getter
@Entity(name = "ReportComment")
@Table(name = "report_comment")
@JsonIdentityInfo(generator = com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ReportComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Nationalized
  @Lob
  @Column(columnDefinition = "LONGTEXT")
  private String content;

  private Date date;

  @ManyToOne
  private Report report;

  public void setContent(String content) {
    this.content = content;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setReport(Report report) {
    this.report = report;
  }
}
