package se.bth.pulse.controller;

import org.hibernate.engine.jdbc.NClobProxy;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Report;
import se.bth.pulse.entity.Report.Status;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.ReportRepository;

@RestController
public class ReportRestController {

  Logger logger = LoggerFactory.getLogger(ReportRestController.class);

  private ProjectRepository projectRepository;

  private ReportRepository reportRepository;

  ReportRestController(ProjectRepository projectRepository, ReportRepository reportRepository) {
    this.projectRepository = projectRepository;
    this.reportRepository = reportRepository;
  }

  private static class Payload {

    public Payload() {}
    private Integer id;
    private String content;
    public void setId(Integer id) {
      this.id = id;
    }
    public void setContent(String content) {
      this.content = content;
    }
    public Integer getId() {
      return id;
    }
    public String getContent() {
      return content;
    }
  }

  @PostMapping("/api/public/report/")
  public ResponseEntity submitReport(@RequestBody Payload payload) {
    try {
      reportRepository.findById(payload.getId()).ifPresent(report -> {
        report.setContent(payload.getContent());
        report.setStatus(Status.SUBMITTED);
        reportRepository.save(report);
      });
      return new ResponseEntity<>(payload.getContent(),HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
