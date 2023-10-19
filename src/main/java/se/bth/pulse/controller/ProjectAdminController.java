package se.bth.pulse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.UserRepository;

/**
 * This class is a view controller that serves the project admin page. The return string is the name
 * of the view to be rendered. The model is used to pass attributes to the view.
 */
@Controller
public class ProjectAdminController {

  private final UserRepository userRepository;

  private final ProjectRepository projectRepository;

  ProjectAdminController(UserRepository userRepository, ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
  }

  /**
   * This function is used to render the project admin page. It uses the project repository to get
   * all the projects.
   *
   * @param model          - used to pass attributes to the view
   * @param authentication - used to get the username and role of the logged-in user
   * @return String        - the view to be rendered
   */
  @GetMapping("/admin/projects")
  public String showProjects(Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("projects", projectRepository.findAll());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "project-admin.jsp");
    return "public/index";
  }

  /**
   * Shows the add project page to the user.
   *
   * @param model          - used to pass attributes to the view
   * @param authentication - used to get the username and role of the logged-in user
   * @return String         - the view to be rendered
   */
  @GetMapping("/admin/project/add")
  public String showAddProjects(Model model, Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("content", "project-admin-add.jsp");
    return "public/index";
  }

  /**
   * This function is used to render the project admin page for editing of a project. It uses the
   * project repository to get the project with the given id. The id is passed as a path variable.
   *
   * @param id             - the id of the project to be edited
   * @param model          - used to pass attributes to the view
   * @param authentication - used to get the username and role of the logged-in user
   * @return String         - the view to be rendered
   */
  @GetMapping("/admin/project/edit/{id}")
  public String showEditProjects(@PathVariable("id") Integer id, Model model,
      Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    projectRepository.findById(id).ifPresent(value -> model.addAttribute("project", value));
    model.addAttribute("content", "project-admin-edit.jsp");
    return "public/index";
  }

  /**
   * This function is used to render the project admin page for editing of a projects members. It
   * uses the project repository to get the project with the given id. The id is passed as a path
   * variable.
   *
   * @param id             - the id of the project to be edited
   * @param model          - used to pass attributes to the view
   * @param authentication - used to get the username and role of the logged-in user
   * @return String           - the view to be rendered
   */
  @GetMapping("/admin/project/users/{id}")
  public String showEditUsersProjects(@PathVariable("id") Integer id, Model model,
      Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    model.addAttribute("users", userRepository.findAll());
    projectRepository.findById(id).ifPresent(value -> model.addAttribute("project", value));
    model.addAttribute("content", "project-admin-users.jsp");
    return "public/index";
  }


  /**
   * This function is used to render the project admin page for deleting of a project. It uses the
   * project repository to get the project with the given id. The id is passed as a path variable.
   *
   * @param id             - the id of the project to be deleted
   * @param model          - used to pass attributes to the view
   * @param authentication - used to get the username and role of the logged-in user
   * @return
   */
  @GetMapping("/admin/project/delete/{id}")
  public String showDeleteProject(@PathVariable("id") Integer id, Model model,
      Authentication authentication) {
    model.addAttribute("user", authentication.getName());
    model.addAttribute("role", authentication.getAuthorities().toString());
    projectRepository.findById(id).ifPresent(value -> model.addAttribute("project", value));
    model.addAttribute("content", "project-admin-delete.jsp");
    return "public/index";
  }
}
