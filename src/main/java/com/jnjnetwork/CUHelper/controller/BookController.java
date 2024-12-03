package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Detail;
import com.jnjnetwork.CUHelper.domain.Question;
import com.jnjnetwork.CUHelper.service.BookService;
import com.jnjnetwork.CUHelper.service.DetailService;
import com.jnjnetwork.CUHelper.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    BookService bookService;
    QuestionService questionService;
    DetailService detailService;

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
    @Transactional
    public String deleteOk(long id) {
        Question question = questionService.findByBookId(id);
        if (question != null) {
            questionService.deleteById(question.getId(), question.getWorksheet());
        }
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
