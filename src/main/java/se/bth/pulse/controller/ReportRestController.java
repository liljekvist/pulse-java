package se.bth.pulse.controller;

import org.quartz.SchedulerException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import se.bth.pulse.repository.ProjectRepository;
@Component
public class ReportRestController {

  private ProjectRepository projectRepository;

  ReportRestController(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

}
