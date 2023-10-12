package se.bth.pulse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.entity.Authority;
import se.bth.pulse.entity.Project;
import se.bth.pulse.entity.Report;
import se.bth.pulse.entity.User;

public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Authority findByName(String name);
}

