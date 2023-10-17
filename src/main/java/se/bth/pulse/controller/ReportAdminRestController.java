package se.bth.pulse.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Optional;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Report;
import se.bth.pulse.entity.ReportComment;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.ReportCommentRepository;
import se.bth.pulse.repository.ReportRepository;
import se.bth.pulse.service.UserDetailsImpl;

@RestController
public class ReportAdminRestController {

  private final ReportRepository reportRepository;

  private final ReportCommentRepository reportCommentRepository;

  Logger logger = LoggerFactory.getLogger(ReportAdminRestController.class);

  public ReportAdminRestController(ReportRepository reportRepository, ReportCommentRepository reportCommentRepository) {
    this.reportRepository = reportRepository;
    this.reportCommentRepository = reportCommentRepository;
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

  @PostMapping("/api/admin/report/comment/{id}")
  public ResponseEntity commentReport(@PathVariable("id") Integer reportId, @RequestBody String content) {
    try {
      ReportComment reportComment = new ReportComment();
      Optional<Report> reportOptional = reportRepository.findById(reportId);
      if (reportOptional.isPresent()) {
        Report r = reportOptional.get();
        reportComment.setContent(content);
        reportComment.setDate(new Date(System.currentTimeMillis()));
        reportComment.setReport(r);
        reportComment = reportCommentRepository.save(reportComment);
      } else {
        throw new RuntimeException("Report not found");
      }

      return new ResponseEntity<>(reportComment, HttpStatus.OK);

    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

}
