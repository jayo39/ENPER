package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Schedule;
import com.jnjnetwork.CUHelper.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    ScheduleRepository scheduleRepository;

    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public List<Schedule> findByUserId(Long user_id) {
        Sort sort = Sort.by(Sort.Order.asc("endTime"));
        return scheduleRepository.findByUserId(user_id, sort);
    }

    @Override
    public List<Schedule> findByUserIdAndIsFinished(Long user_id, boolean isFinished) {
        Sort sort = Sort.by(Sort.Order.asc("endTime"));
        return scheduleRepository.findByUserIdAndIsFinished(user_id, isFinished, sort);
    }

    @Override
    public void save(Schedule schedule) {
        schedule.setIsFinished(false);
        scheduleRepository.saveAndFlush(schedule);
    }

    @Override
    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(Long user_id) {
        scheduleRepository.deleteByUserId(user_id);
    }

    @Override
    public void edit(Long id, LocalTime time, String noteWriting, String note, Boolean isFinished, String studentName, String polishedWriting, String polishedSpeaking) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(RuntimeException::new);
        if (time != null) {
            schedule.setEndTime(time);
        }
        if (noteWriting != null && !(noteWriting.equals(schedule.getNoteWriting()))) {
            schedule.setNoteWriting(noteWriting);
        }
        if (note != null && !(note.equals(schedule.getNote()))) {
            schedule.setNote(note);
        }

        if (isFinished != null) {
            schedule.setIsFinished(isFinished);
        }

        if (studentName != null && !(studentName.equals(schedule.getStudentName()))) {
            schedule.setStudentName(studentName);
        }

        if (polishedWriting != null) {
            if (polishedWriting.isEmpty() || polishedWriting.isBlank()) {
                schedule.setPolishedWriting(null);
            } else if (!(polishedWriting.equals(schedule.getPolishedWriting())) && polishedWriting.length() > 15) {
                schedule.setPolishedWriting(polishedWriting);
            }
        }

        if (polishedSpeaking != null) {
            if (polishedSpeaking.isEmpty() || polishedSpeaking.isBlank()) {
                schedule.setPolishedSpeaking(null);
            } else if (!(polishedSpeaking.equals(schedule.getPolishedSpeaking())) && polishedSpeaking.length() > 15) {
                schedule.setPolishedSpeaking(polishedSpeaking);
            }
        }

        scheduleRepository.saveAndFlush(schedule);
    }
}
