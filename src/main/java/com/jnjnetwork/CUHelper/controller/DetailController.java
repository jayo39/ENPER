package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Detail;
import com.jnjnetwork.CUHelper.service.BookService;
import com.jnjnetwork.CUHelper.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/detail")
public class DetailController {

    BookService bookService;
    DetailService detailService;

    @Autowired
    public void setDetailService(DetailService detailService) {
        this.detailService = detailService;
    }

    @Autowired
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/add")
    public void add(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
    }

    @PostMapping("/addOk")
    public String addOk(@RequestParam("book_id") Long book_id
            ,@RequestParam("firstPage") Long firstPage
            ,@RequestParam("lastPage") Long lastPage
            ,@RequestParam("detail") String detail
            , Model model) {
        Detail detailBuild = Detail.builder()
                .firstPage(firstPage)
                .lastPage(lastPage)
                .detail(detail)
                .build();
        detailService.add(book_id, detailBuild);
        model.addAttribute("book", bookService.findById(book_id));
        return "detail/addOk";
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Detail> list(@RequestParam("book_id") Long book_id, @RequestParam(name = "firstPage", required = false) Long firstPage, @RequestParam(name = "lastPage", required = false) Long lastPage) {
        Book book = bookService.findById(book_id);
        if (firstPage == null || lastPage == null || firstPage < 0 || lastPage < 0) {
            return detailService.findByBook(book);
        }
        return detailService.findDetailByPage(book, firstPage, lastPage);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        Detail detail = detailService.findById(id);
        Book book = detail.getBook();
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("detail", detail);
        model.addAttribute("thisbook", book);
        return "detail/edit";
    }

    @PostMapping("/editOk")
    public String editOk(@RequestParam("book_id") Long book_id
            ,@RequestParam("firstPage") Long firstPage
            ,@RequestParam("lastPage") Long lastPage
            ,@RequestParam("detail") String detail
            ,@RequestParam("id") Long id
            ,Model model) {
        Detail detailBuild = Detail.builder()
                .id(id)
                .firstPage(firstPage)
                .lastPage(lastPage)
                .detail(detail)
                .build();
        Book book = bookService.findById(book_id);
        detailService.edit(detailBuild, book);
        model.addAttribute("book", book);
        return "detail/editOk";
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(Long detail_id) {
        detailService.deleteById(detail_id);
    }
}
