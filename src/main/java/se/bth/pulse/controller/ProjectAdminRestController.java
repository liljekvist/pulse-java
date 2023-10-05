package se.bth.pulse.controller;


import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.Project.ReportInterval;
import se.bth.pulse.entity.Project.WeekDay;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.UserRepository;

/**
 * This class is a REST controller that exposes endpoints for project administration.
 */
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

  /**
   * This subclass is used to parse the JSON payload from the frontend.
   * It is used in the editProjectUsers method.
   * It contains the project id and a list of user ids.
   */
  private static class Payload {

    public Payload() {}

    private Integer id;
    private ArrayList<Integer> userIds;

    public void setUserIds(ArrayList<Integer> userIds) {
      this.userIds = userIds;
    }

    public void setId(Integer id) {
      this.id = id;
    }

    public ArrayList<Integer> getUserIds() {
      return userIds;
    }

    public Integer getId() {
      return id;
    }
  }

  /**
   * This method is used to create a new project.
   * It takes the name, description, report interval and report day as parameters.
   * It returns a response entity with the created project as a JSON object.
   * If the project could not be created, it returns a response entity with an error message.
   *
   * @param name              - the name of the project
   * @param description       - the description of the project
   * @param reportInterval    - the report interval of the project
   * @param reportDay         - the report day of the project
   * @return ResponseEntity   - the response entity returned as a JSON object
   */
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

  /**
   * This method is used to edit a project.
   * It takes the id, name, description, report interval and report day as parameters.
   * It returns a response entity with the edited project as a JSON object.
   * If the project could not be edited, it returns a response entity with an error message.
   *
   * @param id                - the id of the project to be edited
   * @param name              - the new or old name of the project
   * @param description       - the new or old description of the project
   * @param reportInterval    - the new or old report interval of the project
   * @param reportDay         - the new or old report day of the project
   * @return ResponseEntity   - the response entity returned as a JSON object
   */
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

  /**
   * This method is used to edit the users connected to a project.
   * It takes the project id and a list of user ids as parameters.
   * It returns a response entity with the edited project as a JSON object.
   * If the project could not be edited, it returns a response entity with an error message.
   *
   * @param payload - the payload containing the project id and a list of user ids
   * @return ResponseEntity - the response entity returned as a JSON object
   */
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

      List<User> users = userRepository.findAllById(payload.getUserIds());
      projectObj.setUsers(users);

      projectRepository.save(projectObj);
      return new ResponseEntity<>(payload, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>("Error editing project", HttpStatus.BAD_REQUEST);
    }
  }
}
