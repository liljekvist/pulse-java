package se.bth.pulse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.bth.pulse.entity.ReportComment;

/**
 * This is the repository interface for the ReportComment entity.
 */
public interface ReportCommentRepository extends JpaRepository<ReportComment, Integer> {

}

