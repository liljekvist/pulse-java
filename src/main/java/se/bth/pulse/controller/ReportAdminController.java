package se.bth.pulse.controller;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.bth.pulse.entity.Report;
import se.bth.pulse.repository.ReportRepository;

/**
 * This class is a view controller that serves the report admin page.
 */
@Controller
public class ReportAdminController {

  private final ReportRepository reportRepository;

  public ReportAdminController(ReportRepository reportRepository) {
    this.reportRepository = reportRepository;
  }


  /**
   * Renders and views the report admin page.
   * It uses the report repository to get all the reports.
   *
   * @param model             - used to pass attributes to the view
   * @param authentication    - used to get the username and role of the logged-in user
   * @return String           - the view to be rendered
   */
  @GetMapping("/admin/reports")
  public String showReports(Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("reports", reportRepository.findAll());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "report-admin.jsp");
    return "public/index";
  }

  /**
   * Renders a view for viewing a report.
   * It uses the report repository to get the report with the given id.
   *
   * @param id              - the id of the report to be viewed
   * @param model           - used to pass attributes to the view
   * @param authentication  - used to get the username and role of the logged-in user
   * @return String         - the view to be rendered
   */
  @GetMapping("/admin/report/{id}")
  public String showReport(@PathVariable("id") Integer id, Model model, Authentication authentication) {
    model.addAttribute("username", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    Optional<Report> report = reportRepository.findById(id);
    report.ifPresent(value -> model.addAttribute("report", value));
    model.addAttribute("content", "view-report-admin.jsp");
    return "public/index";
  }

}
