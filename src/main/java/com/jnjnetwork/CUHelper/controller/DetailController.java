package com.jnjnetwork.CUHelper.controller;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Detail;
import com.jnjnetwork.CUHelper.service.BookService;
import com.jnjnetwork.CUHelper.service.DetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
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
    public List<Detail> list(@RequestParam("book_id") Long book_id, @RequestParam("firstPage") Long firstPage, @RequestParam("lastPage") Long lastPage) {
        Book book = bookService.findById(book_id);
        return detailService.findDetailByPage(book, firstPage, lastPage);
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(Long detail_id) {
        detailService.deleteById(detail_id);
    }
}
