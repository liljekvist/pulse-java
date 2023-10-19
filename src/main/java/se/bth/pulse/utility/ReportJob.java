package se.bth.pulse.utility;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.bth.pulse.entity.Report;
import se.bth.pulse.entity.Report.Status;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.ReportRepository;

/**
 * This class is used to create reports for projects. It is scheduled to run the same time the last
 * report is due or when the project starts.
 */
@Component
public class ReportJob implements Job {

  Logger logger = LoggerFactory.getLogger(ReportJob.class);

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  private ReportRepository reportRepository;

  /**
   * Is run when a job is set to be executed.
   *
   * @param context - The context of the job. Contains the project id.
   * @throws JobExecutionException - If the job fails to execute.
   */
  public void execute(JobExecutionContext context) throws JobExecutionException {
    logger.info("Report job started");
    try {
      LocalDateTime now = LocalDateTime.now();
      DayOfWeek dow = now.getDayOfWeek();

      if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) {
        return;
      }

      JobDataMap dataMap = context.getJobDetail().getJobDataMap();
      int id = dataMap.getInt("project_id");
      projectRepository.findById(id).ifPresent(project -> {
        Report report = new Report();
        report.setDueDate(context.getTrigger().getNextFireTime());
        report.setName("Report for " + project.getName());
        report.setStatus(Status.MISSING);
        report.setProject(project);
        reportRepository.save(report);
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }
}
