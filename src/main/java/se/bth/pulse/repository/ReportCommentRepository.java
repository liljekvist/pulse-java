package se.bth.pulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.entity.Authority;
import se.bth.pulse.entity.ReportComment;

public interface ReportCommentRepository extends JpaRepository<ReportComment, Integer> {

}

