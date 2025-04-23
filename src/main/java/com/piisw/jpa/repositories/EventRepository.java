package com.piisw.jpa.repositories;

import com.piisw.jpa.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByTimeBetweenAndAnalysisRequired(LocalDateTime start, LocalDateTime end, boolean toBeAnalyzed, Pageable pageable);

    @Modifying
    @Query("delete from Event e where e.time < :timeLimit")
    void deleteInBulkBeforDate(@Param("timeLimit") LocalDateTime timeLimit);

    @Modifying
    @Query("UPDATE Event e " +
            "SET e.analysisRequired = true " +
            "WHERE TYPE(e) = :clazz AND e.duration > :minDuration")
    void updateInBulkToBeAnalyzedByType(@Param("clazz") Class<? extends Event> clazz, @Param("minDuration") int minDuration);

    @Query("""
SELECT new com.piisw.jpa.repositories.ServerStatistic(s, COUNT(e))
FROM Server s JOIN Event e ON e.server = s
WHERE s.isActive = true
GROUP BY s
""")
    List<ServerStatistic> countEventsPerServer();
}
