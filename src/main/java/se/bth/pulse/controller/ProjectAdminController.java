package se.bth.pulse.controller;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.bth.pulse.entity.Project;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.UserRepository;
@Controller
public class ProjectAdminController {

  private final UserRepository userRepository;

  private final ProjectRepository projectRepository;

  ProjectAdminController(UserRepository userRepository, ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
  }
  @GetMapping("/admin/projects")
  public String showProjects(Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("users", userRepository.findAll());
    model.addAttribute("projects", projectRepository.findAll());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "project-admin.jsp");
    return "public/index";
  }

  @GetMapping("/admin/projects/add")
  public String showAddProjects(Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "project-admin-add.jsp");
    return "public/index";
  }

  @GetMapping("/admin/projects/edit/{id}")
  public String showEditProjects(@PathVariable("id") Integer id, Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    projectRepository.findById(id).ifPresent(value -> model.addAttribute("project", value));
    model.addAttribute("content", "project-admin-edit.jsp");
    return "public/index";
  }

  @GetMapping("/admin/projects/users/{id}")
  public String showEditUsersProjects(@PathVariable("id") Integer id, Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("users", userRepository.findAll());
    projectRepository.findById(id).ifPresent(value -> model.addAttribute("project", value));
    model.addAttribute("content", "project-admin-users.jsp");
    return "public/index";
  }
}
