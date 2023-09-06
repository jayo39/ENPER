package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Detail;
import com.jnjnetwork.CUHelper.service.BookService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {

    BookService bookService;

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
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
        List<Detail> details = book.getDetails();
        model.addAttribute("book", book);
        model.addAttribute("details", details);
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
        bookService.deleteById(id);
        return "book/deleteOk";
    }

    @PostMapping("/search")
    public String searchOk(@RequestParam("searchedValue") String keyword, Model model) {
        List<Book> books = bookService.findByKeyword(keyword);
        model.addAttribute("searchedBooks", books);
        return "index";
    }
}
