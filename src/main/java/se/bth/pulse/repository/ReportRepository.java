package se.bth.pulse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.Report;
import se.bth.pulse.entity.User;

/**
 * This interface is used to interact with the table Project. Using JpaRepository we can perform
 * CRUD operations. We can also define custom methods here with querys.
 */
public interface ReportRepository extends JpaRepository<Report, Integer> {

  List<Report> findAllByProjectUsersIn(List<User> users);

  Report findByIdAndProjectUsersIn(Integer id, List<User> users);

  void deleteAllByProject(Project project);

  Report getSingleByProjectOrderByDueDate(Project project);
}
