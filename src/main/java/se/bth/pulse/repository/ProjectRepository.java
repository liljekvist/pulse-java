package se.bth.pulse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.User;

/**
 * This interface is used to interact with the table Project.
 * Using JpaRepository we can perform CRUD operations.
 * We can also define custom methods here with querys.
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {

  Project findByName(String name);

}
