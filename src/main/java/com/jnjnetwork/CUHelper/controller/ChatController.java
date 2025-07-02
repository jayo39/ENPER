package com.jnjnetwork.CUHelper.controller;


import com.jnjnetwork.CUHelper.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    GeminiService geminiService;

    @Autowired
    public void setGeminiService(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/student-note")
    public ResponseEntity<String> convertStudentNote(@RequestParam("studentName") String studentName, @RequestParam("note") String roughNotes, @RequestParam("type") String type) {
        if(roughNotes.isEmpty()) {
            return null;
        }
        String polished = geminiService.generateFeedback(studentName, roughNotes, type);
        return ResponseEntity.ok(polished);
    }
}
