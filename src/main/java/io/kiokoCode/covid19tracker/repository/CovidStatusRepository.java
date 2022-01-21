package io.kiokoCode.covid19tracker.repository;

import io.kiokoCode.covid19tracker.model.CovidStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author kioko
 */

@Repository
public interface CovidStatusRepository extends JpaRepository<CovidStats, Long> {
}
