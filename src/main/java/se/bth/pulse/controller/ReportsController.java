package se.bth.pulse.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.Report;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.ReportRepository;
import se.bth.pulse.service.UserDetailsImpl;

/**
 * This class is a view controller that serves the reports page.
 */
@Controller
public class ReportsController {

  private final ReportRepository reportRepository;

  private final ProjectRepository projectRepository;

  public ReportsController(ReportRepository reportRepository, ProjectRepository projectRepository) {
    this.reportRepository = reportRepository;
    this.projectRepository = projectRepository;
  }


  /**
   * This serves the reports page to the user.
   * The return string is the name of the view to be rendered.
   *
   * @param model          - used to pass attributes to the view
   * @param authentication - used to get the username and role of the logged-in user
   * @return String        - the view to be rendered
   */
  @GetMapping("/reports")
  public String showReports(Model model, Authentication authentication) {
    model.addAttribute("username", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    List<User> users = new ArrayList<>();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    users.add(userDetails.getUser());
    List<Report> reports = reportRepository.findAllByProjectUsersIn(users);
    model.addAttribute("reports", reports);
    model.addAttribute("content", "reports.jsp");
    return "public/index";
  }

  @GetMapping("/report/{id}")
  public String showReport(@PathVariable("id") Integer id, Model model, Authentication authentication) {
    model.addAttribute("username", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    List<User> users = new ArrayList<>();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    users.add(userDetails.getUser());
    Report report = reportRepository.findByIdAndProjectUsersIn(id, users);
    model.addAttribute("report", report);
    model.addAttribute("content", "view-report.jsp");
    return "public/index";
  }

}
