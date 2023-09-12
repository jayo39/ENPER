package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Detail;

import java.util.List;

public interface DetailService {
    void add(Long book_id, Detail detail);
    void deleteById(Long detail_id);
    List<Detail> findDetailByPage(Book book, Long firstPage, Long lastPage);
    List<Detail> findByBook(Book book);
    Detail findById(Long detail_id);
    void edit(Detail detail, Book book);
}
