package se.bth.pulse.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Optional;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Report;
import se.bth.pulse.repository.ReportRepository;

@RestController
public class ReportAdminRestController {

  private final ReportRepository reportRepository;

  Logger logger = LoggerFactory.getLogger(ReportAdminRestController.class);

  public ReportAdminRestController(ReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }

  @PostMapping("/api/admin/report/read/{id}")
  public ResponseEntity readReport(@PathVariable("id") Integer id) {
    try {
      Optional<Report> reportOptional = reportRepository.findById(id);
      reportOptional.ifPresent(report -> {
        report.setStatus(Report.Status.READ);
        reportRepository.save(report);
      });
      reportOptional.orElseThrow(() -> new RuntimeException("Report not found"));

      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


  }

}
