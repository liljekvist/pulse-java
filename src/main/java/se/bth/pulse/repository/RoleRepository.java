package se.bth.pulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.entity.Role;

/**
 * This interface is used to interact with the table Role. Using JpaRepository we can perform CRUD
 * operations. We can also define custom methods here with querys.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

  Role findByName(String role);
}
