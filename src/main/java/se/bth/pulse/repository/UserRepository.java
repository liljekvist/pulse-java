package se.bth.pulse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.User;

/**
 * This interface is used to interact with the table User.
 * Using JpaRepository we can perform CRUD operations.
 * We can also define custom methods here with querys.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  @Query("SELECT u FROM User u WHERE u.email = :username")
  User getUserByUsername(@Param("username") String username);

  List<User> findByProjects(Project project);

  User findByEmail(String email);
}