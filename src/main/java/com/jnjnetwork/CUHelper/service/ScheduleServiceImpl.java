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
    public void save(Schedule schedule) {
        schedule.setIsFinished(false);
        scheduleRepository.saveAndFlush(schedule);
    }

    @Override
    public void deleteById(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        scheduleRepository.deleteAll();
    }

    @Override
    public void edit(Long id, LocalTime time, String content, Boolean isFinished) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(RuntimeException::new);
        if (time != null) {
            schedule.setEndTime(time);
        }
        if (content != null && !(content.equals(schedule.getNote()))) {
            schedule.setNote(content);
        }

        if (isFinished != null) {
            schedule.setIsFinished(isFinished);
        }

        scheduleRepository.saveAndFlush(schedule);
    }
}
