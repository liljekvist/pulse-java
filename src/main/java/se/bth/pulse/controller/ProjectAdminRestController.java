package se.bth.pulse.controller;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.Project.ReportInterval;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.UserRepository;
import se.bth.pulse.utility.ReportJob;

/**
 * This class is a REST controller that exposes endpoints for project administration.
 */
@RestController
@OpenAPIDefinition(info = @Info(title = "ProjectAdminRestController", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class ProjectAdminRestController {

  private final ProjectRepository projectRepository;

  private SchedulerFactoryBean schedulerFactoryBean;

  private final UserRepository userRepository;

  ProjectAdminRestController(ProjectRepository projectRepository, SchedulerFactoryBean schedulerFactoryBean,
      UserRepository userRepository)
      throws SchedulerException {
    this.schedulerFactoryBean = schedulerFactoryBean;
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
   * @return ResponseEntity   - the response entity returned as a JSON object
   */
  @PostMapping("/api/admin/project/add")
  public ResponseEntity createProject(String name, String description, ReportInterval reportInterval, @DateTimeFormat(iso = ISO.DATE_TIME) Date startDate, @DateTimeFormat(iso = ISO.DATE_TIME) Date endDate) {

    Scheduler scheduler = schedulerFactoryBean.getScheduler();
    try {
      Project project = new Project();
      project.setName(name);
      project.setDescription(description);
      project.setReportInterval(reportInterval);
      project.setStartDate(startDate);
      project.setEndDate(endDate);
      projectRepository.save(project);


      JobKey jobKey = new JobKey(name + "_" + project.getId() + "_reports", "reports");
      JobDetail job = JobBuilder.newJob(ReportJob.class)
          .usingJobData("project_id", project.getId())
          .withIdentity(jobKey)
          .build();

      job.getJobDataMap().put("project_id", project.getId());
      job.getJobDataMap().put("report_interval", project.getReportInterval().getHours());

      Trigger trigger = TriggerBuilder
          .newTrigger()
          .withIdentity(name + "_" + project.getId() + "_report_trigger", "reports")
          .startAt(startDate)
          .endAt(endDate)
          .withSchedule(
              simpleSchedule().withIntervalInHours(reportInterval.getHours()).repeatForever()
                  .withMisfireHandlingInstructionFireNow()
          )
          .build();


      scheduler.scheduleJob(job, trigger);

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
   * @return ResponseEntity   - the response entity returned as a JSON object
   */
  @PostMapping("/api/admin/project/edit")
  public ResponseEntity editProject(Integer id, String name, String description, ReportInterval reportInterval, Date startDate, Date endDate) {

    try {
      Optional<Project> project = projectRepository.findById(id);
      if (project.isEmpty()) {
        return new ResponseEntity<>("Project not found", HttpStatus.BAD_REQUEST);
      }
      Project projectObj = project.get();
      projectObj.setName(name);
      projectObj.setDescription(description);
      projectObj.setReportInterval(reportInterval);
      projectObj.setStartDate(startDate);
      projectObj.setEndDate(endDate);

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
