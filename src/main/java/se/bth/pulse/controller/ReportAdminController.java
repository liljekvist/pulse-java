package se.bth.pulse.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.bth.pulse.entity.Report;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.ReportRepository;
import se.bth.pulse.service.UserDetailsImpl;

@Controller
public class ReportAdminController {

  private final ProjectRepository projectRepository;

  private final ReportRepository reportRepository;

  public ReportAdminController(ReportRepository reportRepository, ProjectRepository projectRepository) {
    this.reportRepository = reportRepository;
    this.projectRepository = projectRepository;
  }


  @GetMapping("/admin/reports")
  public String showProjects(Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("reports", reportRepository.findAll());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "report-admin.jsp");
    return "public/index";
  }

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
