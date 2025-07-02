package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Schedule;

import java.time.LocalTime;
import java.util.List;

public interface ScheduleService {
    List<Schedule> findByUserId(Long user_id);
    List<Schedule> findByUserIdAndIsFinished(Long user_id, boolean isFinished);
    void save(Schedule schedule);
    void deleteById(Long id);
    void deleteByUserId(Long user_id);
    void edit(Long id, LocalTime time, String noteWriting, String note, Boolean isFinished, String studentName, String polishedWriting, String polishedSpeaking);
}
