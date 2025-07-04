package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Schedule;
import com.jnjnetwork.CUHelper.domain.User;
import com.jnjnetwork.CUHelper.service.ScheduleService;
import com.jnjnetwork.CUHelper.util.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    ScheduleService scheduleService;

    @Autowired
    public void setScheduleService(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/list")
    public List<Schedule> list(@RequestParam("user_id") Long user_id) {
        return scheduleService.findByUserId(user_id);
    }

    @PostMapping("/add")
    @Transactional
    public Map<String, Object> addOk(Schedule schedule, BindingResult result) {
        User user = U.getLoggedUser();
        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors()) {
            response.put("status", "fail");
            return response;
        }
        response.put("status", "success");
        schedule.setUser(user);
        scheduleService.save(schedule);
        return response;
    }

    @PostMapping("/edit")
    @Transactional
    public Map<String, Object> editOk(
            @RequestParam("schedule_id")     Long    scheduleId,
            @RequestParam(name = "time",         required = false) LocalTime time,
            @RequestParam(name = "noteWriting",  required = false) String  noteWriting,
            @RequestParam(name = "note",         required = false) String  note,
            @RequestParam(name = "isFinished",   required = false) Boolean isFinished,
            @RequestParam(name = "name",         required = false) String  studentName,
            @RequestParam(name = "polishedWriting",  required = false) String  polishedWriting,
            @RequestParam(name = "polishedSpeaking", required = false) String  polishedSpeaking
    ) {
        Map<String, Object> response = new HashMap<>();
        scheduleService.edit(
                scheduleId,
                time,
                noteWriting,
                note,
                isFinished,
                studentName,
                polishedWriting,
                polishedSpeaking
        );
        response.put("status", "success");
        return response;
    }

    @PostMapping("/delete")
    @Transactional
    public void deleteOk(Long id) {
        scheduleService.deleteById(id);
    }

    @PostMapping("/clear")
    @Transactional
    public void clearOk() {
        User user = U.getLoggedUser();
        scheduleService.deleteByUserId(user.getId());
    }
}
