package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.History;
import com.jnjnetwork.CUHelper.domain.HistoryDTO;
import com.jnjnetwork.CUHelper.service.BookService;
import com.jnjnetwork.CUHelper.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/history")
public class HistoryController {

    HistoryService historyService;
    BookService bookService;

    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public List<HistoryDTO> list(@RequestParam(value="user_id",required = false) Long userId) {
        List<History> histories;
        if (userId == null) {
            histories = historyService.findAll();
        } else {
            histories = historyService.findByUserId(userId);
        }
        return histories.stream()
            .map(history -> new HistoryDTO(
                    history.getId(),
                    history.getUser().getUsername(),
                    history.getBook().getTitle(),
                    history.getBook().getSeries(),
                    history.getAccessDate()
            ))
            .collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public void deleteOk(Long id) {
        historyService.deleteById(id);
    }

    @PostMapping("/clear")
    public void clearOk() {
        historyService.deleteAll();
    }
}
