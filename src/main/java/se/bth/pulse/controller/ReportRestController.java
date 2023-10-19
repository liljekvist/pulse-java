package se.bth.pulse.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Report.Status;
import se.bth.pulse.repository.ReportRepository;

/**
 * This class is a rest controller that serves the report page.
 */
@RestController
public class ReportRestController {

  Logger logger = LoggerFactory.getLogger(ReportRestController.class);

  private ReportRepository reportRepository;

  ReportRestController(ReportRepository reportRepository) {
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

  /**
   * This method is used to submit a report.
   * The report is retrieved from the report repository using the id.
   * The report content is updated and the status is set to submitted.
   * The report saves to the report repository.
   *
   * @param payload           - the payload containing the report id and content
   * @return ResponseEntity   - the response entity containing a success message if the report
   */
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
