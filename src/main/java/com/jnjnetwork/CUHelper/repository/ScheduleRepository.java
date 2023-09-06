package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Schedule;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserId(Long user_id, Sort sort);
    void deleteByUserId(Long user_id);
}
