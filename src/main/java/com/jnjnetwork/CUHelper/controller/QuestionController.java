package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Question;
import com.jnjnetwork.CUHelper.service.BookService;
import com.jnjnetwork.CUHelper.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/question")
public class QuestionController {
    BookService bookService;
    QuestionService questionService;

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @Autowired
    public void setQuestionService(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/add")
    public void add(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
    }

    @PostMapping("/addOk")
    @Transactional
    public String addOk(@RequestParam("upfile") MultipartFile file, @RequestParam("book_id") Long book_id
            , @RequestParam("question") String content
            , Model model) {
        Book book = bookService.findById(book_id);
        Question questionBuilder = Question.builder()
                .book(book)
                .content(content)
                .worksheet(file.toString())
                .build();

        questionService.add(questionBuilder, file);
        book.setQuestion(questionBuilder);
        bookService.save(book);
        model.addAttribute("book", bookService.findById(book_id));
        return "question/addOk";
    }

    @PostMapping("/delete")
    @Transactional
    public String deleteOk(long id, long book_id, String fileName, Model model) {
        Book book = bookService.findById(book_id);
        book.setQuestion(null);
        bookService.save(book);
        questionService.deleteById(id, fileName);
        model.addAttribute("book", bookService.findById(book_id));
        return "question/deleteOk";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        List<Book> books = bookService.findAll();
        Question question = questionService.findById(id);
        model.addAttribute("books", books);
        model.addAttribute("question", question);
        return "question/edit";
    }

    @PostMapping("/edit")
    public String updateOk(@RequestParam("upfile") MultipartFile file,
                           @RequestParam("book_id") Long book_id,
                           @RequestParam("id") Long id,
                           @RequestParam("question") String content,
                           Model model) {
        Book book = bookService.findById(book_id);
        Question originalQuestion = questionService.findById(id);

        if(file == null) {
            originalQuestion.setContent(content);
            questionService.add(originalQuestion, null);
        } else {
            originalQuestion.setContent(content);
            originalQuestion.setWorksheet(file.toString());
            questionService.add(originalQuestion, file);
        }
        model.addAttribute("book", book);
        return "question/editOk";
    }
}
