package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import org.springframework.ui.Model;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    List<Book> findAll(Integer page, Model model);
    void save(Book book);
    Book findById(Long id);
    void deleteById(Long id);
    void edit(Book book);
    List<Book> findByKeyword(String keyword);
}
