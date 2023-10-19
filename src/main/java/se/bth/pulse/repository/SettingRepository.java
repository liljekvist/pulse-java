package se.bth.pulse.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.entity.Setting;

/**
 * This interface is used to interact with the table Settings. Using JpaRepository we can perform
 * CRUD operations. We can also define custom methods here with querys.
 */
public interface SettingRepository extends JpaRepository<Setting, Integer> {

  List<Setting> findByName(String value);
}
