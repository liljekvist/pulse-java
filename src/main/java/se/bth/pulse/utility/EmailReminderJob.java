package se.bth.pulse.utility;

import java.util.ArrayList;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import se.bth.pulse.entity.User;
import se.bth.pulse.repository.ProjectRepository;
import se.bth.pulse.repository.UserRepository;

/**
 * This class is used to send email reminders to users.
 * It is scheduled to run 24 hours before a report is due.
 * The job and trigger is created in the project admin controller.
 */
@Component
public class EmailReminderJob implements Job {

  Logger logger = LoggerFactory.getLogger(EmailReminderJob.class);

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private JavaMailSender emailSender;

  @Autowired
  private UserRepository userRepository;

  /**
   * Is run when a job is set to be executed.
   *
   * @param context - The context of the job. Contains the project id.
   * @throws JobExecutionException - If the job fails to execute.
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    logger.info("Email reminder job started");
    try {
      JobDataMap dataMap = context.getJobDetail().getJobDataMap();
      int id = dataMap.getInt("project_id");
      projectRepository.findById(id).ifPresent(project -> {
        ArrayList<User> users = new ArrayList<>(userRepository.findByProjects(project));
        for (User user : users) {
          if (user.getEmail() != null) {
            try {
              SimpleMailMessage message = new SimpleMailMessage();
              message.setTo(user.getEmail());
              message.setFrom("projectpulse1337@gmail.com");
              message.setSubject("Pulse - Report reminder");
              message.setText(String.format(
                  "Hello %s,\n\nThis is a reminder to fill in your report for the project %s. Its due %s.\n\nBest regards,\nPulse team",
                  user.getEmail(), project.getName(), context.getNextFireTime()));
              emailSender.send(message);
            } catch (Exception e) {
              logger.error("Error sending email: " + e.getMessage());
              throw e;
            }
          }
        }
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

}
