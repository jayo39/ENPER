package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.History;
import com.jnjnetwork.CUHelper.domain.HistoryDTO;
import com.jnjnetwork.CUHelper.domain.User;
import com.jnjnetwork.CUHelper.service.BookService;
import com.jnjnetwork.CUHelper.service.HistoryService;
import com.jnjnetwork.CUHelper.util.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Transactional
    public void deleteOk(Long id) {
        historyService.deleteById(id);
    }

    @PostMapping("/clear")
    @Transactional
    public void clearOk() {
        historyService.deleteAll();
    }
}
