package se.bth.pulse.utility;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.bth.pulse.entity.Project;
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
    JobDataMap dataMap = context.getJobDetail().getJobDataMap();
    int id = dataMap.getInt("project_id");
    projectRepository.findById(id).ifPresent(project -> {
      logger.info("Project: " + project.getName());
    });
    logger.info("Hello Quartz!");
  }
}
