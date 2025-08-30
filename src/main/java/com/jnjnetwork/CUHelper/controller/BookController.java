package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.*;
import com.jnjnetwork.CUHelper.service.*;
import com.jnjnetwork.CUHelper.util.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    BookService bookService;
    QuestionService questionService;
    DetailService detailService;
    HistoryService historyService;

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }
    @Autowired
    public void setDetailService(DetailService detailService) {
        this.detailService = detailService;
    }
    @Autowired
    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/add")
    public void add() {;}

    @PostMapping("/addOk")
    public String addOk(Book book) {
        bookService.save(book);
        return "book/addOk";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable long id, Model model) {
        Book book = bookService.findById(id);
        List<Detail> details;
        if (book != null) {
            details = book.getDetails();
        } else {
            details = null;
        }
        Question question = questionService.findByBookId(id);

        // Log history here
        try {
            User user;
            try {
                user = U.getLoggedUser();
            } catch (Exception e) {
                return null;
            }
            if (user != null && book != null) {
                History history = new History();
                history.setUser(user);
                history.setBook(book);
                history.setAccessDate(LocalDateTime.now());
                historyService.save(history);
            }
        } catch (Exception e) {
            // user not logged in, do nothing for now
        }

        model.addAttribute("book", book);
        model.addAttribute("details", details);
        model.addAttribute("question", question);
        return "book/detail";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "book/edit";
    }

    @PostMapping("/edit")
    public String updateOk(Book book) {
        bookService.edit(book);
        return "book/editOk";
    }

    @PostMapping("/delete")
    public String deleteOk(long id) {
        Book book = bookService.findById(id);
        Question question = questionService.findByBookId(id);
        if (question != null) {
            book.setQuestion(null);
            bookService.save(book);
            questionService.deleteById(question.getId(), question.getWorksheet());
        }
        historyService.deleteByBookId(id);
        detailService.deleteByBookId(id);
        bookService.deleteById(id);
        return "book/deleteOk";
    }

    @GetMapping("/search")
    public String searchOk(@RequestParam("value") String keyword, Model model) {
        if (keyword.isEmpty()) {
            return "redirect:/";
        } else if (keyword.length() == 1) {
            return "error/404";
        }
        List<Book> books = bookService.findByKeyword(keyword);
        model.addAttribute("searchedBooks", books);
        model.addAttribute("keyword", keyword);
        return "index";
    }
}
