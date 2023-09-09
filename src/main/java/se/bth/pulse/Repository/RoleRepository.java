package se.bth.pulse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.Entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
