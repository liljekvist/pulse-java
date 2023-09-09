package se.bth.pulse.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.Entity.Setting;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, Integer> {
    List<Setting> findByName(String value);
}
