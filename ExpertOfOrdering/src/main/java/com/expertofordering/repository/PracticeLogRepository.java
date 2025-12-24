package com.expertofordering.repository;

import com.expertofordering.entity.PracticeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeLogRepository extends JpaRepository<PracticeLog, Long> {
    List<PracticeLog> findByUserIdAndMonth(Long userId, int month);
}
