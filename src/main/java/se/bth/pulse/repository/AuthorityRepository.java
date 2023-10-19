package se.bth.pulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.entity.Authority;

/**
 * This is the repository interface for the authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Authority findByName(String name);
}

