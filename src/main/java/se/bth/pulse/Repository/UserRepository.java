package se.bth.pulse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}