package se.bth.pulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * PulseApplication class. Contains the main entrypoint to the application. Extends
 * SpringBootServletInitializer to allow the application to be deployed as a WAR file.
 */
@SpringBootApplication
public class PulseApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(PulseApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(PulseApplication.class);
  }

}
