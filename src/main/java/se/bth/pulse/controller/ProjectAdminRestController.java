package se.bth.pulse.controller;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.Project.ReportInterval;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.ReportRepository;
import se.bth.pulse.repository.UserRepository;
import se.bth.pulse.utility.EmailReminderJob;
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

  private final ReportRepository reportRepository;

  Logger logger = LoggerFactory.getLogger(ProjectAdminRestController.class);

  ProjectAdminRestController(ProjectRepository projectRepository, SchedulerFactoryBean schedulerFactoryBean,
      UserRepository userRepository, ReportRepository reportRepository)
      throws SchedulerException {
    this.reportRepository = reportRepository;
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


      JobKey jobKey = new JobKey(project.getId() + "_reports", project.getId() + "_report_group");
      JobDetail job = JobBuilder.newJob(ReportJob.class)
          .usingJobData("project_id", project.getId())
          .withIdentity(jobKey)
          .storeDurably(true)
          .build();

      job.getJobDataMap().put("project_id", project.getId());

      Trigger trigger = TriggerBuilder
          .newTrigger()
          .withIdentity(project.getId() + "_report_trigger", project.getId() + "_report_group_trigger")
          .startAt(startDate)
          .endAt(endDate)
          .withPriority(2)
          .withSchedule(
              simpleSchedule().withIntervalInHours(reportInterval.getHours())
                  .withMisfireHandlingInstructionFireNow()
                  .repeatForever()
                  .withRepeatCount(-1)
          )
          .build();

      var date = scheduler.scheduleJob(job, trigger);

      JobKey jobKeyReminder = new JobKey(project.getId() + "_reports_reminder", project.getId() + "_report_reminder_group");
      JobDetail jobReminder = JobBuilder.newJob(EmailReminderJob.class)
          .usingJobData("project_id", project.getId())
          .storeDurably(true)
          .withIdentity(jobKeyReminder)
          .build();

      jobReminder.getJobDataMap().put("project_id", project.getId());

      LocalDateTime updatedTime;
      LocalDateTime dateTime = startDate.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();

      updatedTime = dateTime.minusHours(24);

      Trigger triggerReminder = TriggerBuilder
          .newTrigger()
          .withIdentity(project.getId() + "_report_trigger_reminder", project.getId() + "_report_reminder_group_trigger")
          .startAt(Timestamp.valueOf(updatedTime))
          .endAt(endDate)
          .withPriority(1)
          .withSchedule(
              simpleSchedule().withIntervalInHours(reportInterval.getHours())
                  .withMisfireHandlingInstructionFireNow()
                  .repeatForever()
                  .withRepeatCount(-1)
          )
          .build();

      scheduler.scheduleJob(jobReminder, triggerReminder);

      return new ResponseEntity<>(project, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
  public ResponseEntity editProject(Integer id, String name, String description, ReportInterval reportInterval, @DateTimeFormat(iso = ISO.DATE_TIME) Date startDate, @DateTimeFormat(iso = ISO.DATE_TIME) Date endDate) {

    try {
      Scheduler scheduler = schedulerFactoryBean.getScheduler();
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

      Trigger oldTrigger = scheduler.getTrigger(
          TriggerKey.triggerKey(projectObj.getId() + "_report_trigger", projectObj.getId() + "_report_group_trigger"));

      TriggerBuilder newTriggerBuilder = oldTrigger.getTriggerBuilder();

      Trigger newTrigger = newTriggerBuilder
          .startAt(startDate)
          .endAt(endDate)
          .withSchedule(
              simpleSchedule().withIntervalInHours(reportInterval.getHours()).repeatForever()
                  .withMisfireHandlingInstructionFireNow()
          )
          .build();

      scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);

      Trigger oldTriggerEmail = scheduler.getTrigger(
          TriggerKey.triggerKey(projectObj.getId() + "_report_trigger_reminder", projectObj.getId() + "_report_reminder_group_trigger"));

      TriggerBuilder newTriggerBuilderEmail = oldTriggerEmail.getTriggerBuilder();

      LocalDateTime updatedTime;
      LocalDateTime dateTime = startDate.toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();

      updatedTime = dateTime.minusHours(24);

      Trigger newTriggerEmail = newTriggerBuilderEmail
          .startAt(Timestamp.valueOf(updatedTime))
          .endAt(endDate)
          .withSchedule(
              simpleSchedule().withIntervalInHours(reportInterval.getHours()).repeatForever()
                  .withMisfireHandlingInstructionFireNow()
          )
          .build();

      scheduler.rescheduleJob(oldTriggerEmail.getKey(), newTriggerEmail);

      return new ResponseEntity<>(project, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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

  @Transactional
  @PostMapping("/api/admin/project/delete/{id}")
  public ResponseEntity deleteUser(@PathVariable("id") Integer id) {
    try {
      Optional<Project> project = projectRepository.findById(id);
      if (project.isPresent()) {
        Project p = project.get();

        reportRepository.deleteAllByProject(p);

        projectRepository.delete(p);

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.deleteJob(new JobKey(p.getId() + "_reports", p.getId() + "_report_group"));

        scheduler.deleteJob(new JobKey(p.getId() + "_reports_reminder", p.getId() + "_report_reminder_group"));



        return new ResponseEntity<>(HttpStatus.OK);
      } else {
        throw new IllegalArgumentException("User not found");
      }
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
