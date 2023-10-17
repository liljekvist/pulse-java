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
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.Project.ReportInterval;
import se.bth.pulse.entity.Report;
import se.bth.pulse.entity.Report.Status;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.ReportRepository;

@Component
public class ReportJob implements Job {

  Logger logger = LoggerFactory.getLogger(ReportJob.class);

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  private ReportRepository reportRepository;

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
