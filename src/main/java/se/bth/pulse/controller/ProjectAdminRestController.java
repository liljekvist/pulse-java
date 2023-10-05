package se.bth.pulse.controller;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.Project.ReportInterval;
import se.bth.pulse.entity.Project.WeekDay;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.UserRepository;

@RestController
@OpenAPIDefinition(info = @Info(title = "ProjectAdminRestController", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class ProjectAdminRestController {

  private final ProjectRepository projectRepository;

  private final UserRepository userRepository;

  ProjectAdminRestController(ProjectRepository projectRepository, UserRepository userRepository) {
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
  }

  private static class Payload {

    public Payload() {}

    private Integer id;
    private ArrayList<Integer> user_ids;

    public ArrayList<Integer> getUser_ids() {
      return user_ids;
    }

    public void setUser_ids(ArrayList<Integer> user_ids) {
      this.user_ids = user_ids;
    }

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }
  }

  @PostMapping("/api/admin/project/add")
  public ResponseEntity createProject(String name, String description, ReportInterval reportInterval, WeekDay reportDay) {

    try {
      Project project = new Project();
      project.setName(name);
      project.setDescription(description);
      project.setReportInterval(reportInterval);
      project.setReportDay(reportDay);
      projectRepository.save(project);
      return new ResponseEntity<>(project, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error creating project", HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/api/admin/project/edit")
  public ResponseEntity editProject(Integer id, String name, String description, ReportInterval reportInterval, WeekDay reportDay) {

    try {
      Optional<Project> project = projectRepository.findById(id);
      if (project.isEmpty()) {
        return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
      }
      Project projectObj = project.get();
      projectObj.setName(name);
      projectObj.setDescription(description);
      projectObj.setReportInterval(reportInterval);
      projectObj.setReportDay(reportDay);

      projectRepository.save(projectObj);
      return new ResponseEntity<>(project, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error creating project", HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/api/admin/project/users",
                consumes = "application/json",
                produces = "application/json"
            )
  public ResponseEntity editProjectUsers(@RequestBody Payload payload) {

    try {
      Optional<Project> project = projectRepository.findById(payload.getId());
      if (project.isEmpty()) {
        return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
      }

      Project projectObj = project.get();

      //List<Integer> userIdsInt = user_ids.stream().mapToInt(Integer::parseInt).boxed().toList();

      List<User> users = userRepository.findAllById(payload.getUser_ids());
      projectObj.setUsers(users);

      projectRepository.save(projectObj);
      return new ResponseEntity<>(payload, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error creating project", HttpStatus.BAD_REQUEST);
    }
  }
}
