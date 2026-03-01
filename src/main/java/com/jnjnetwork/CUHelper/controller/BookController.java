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
    UserService userService;

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
    @Autowired
    public void setUserService(UserService userService) { this.userService = userService; }

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
        if (book == null) return "error/404";

        List<Detail> details = book.getDetails();
        Question question = questionService.findByBookId(id);

        User user = U.getLoggedUser();
        if (user != null) {
            User freshUser = userService.findById(user.getId()).orElseThrow();
            model.addAttribute("currentUser", freshUser);

            History history = new History();
            history.setUser(user);
            history.setBook(book);
            history.setAccessDate(LocalDateTime.now());
            historyService.save(history);
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
        User sessionUser = U.getLoggedUser();
        if (sessionUser != null) {
            model.addAttribute("currentUser", userService.findByUsername(sessionUser.getUsername()));
        }
        List<Book> books = bookService.findByKeyword(keyword);
        model.addAttribute("searchedBooks", books);
        model.addAttribute("keyword", keyword);
        return "index";
    }
}
