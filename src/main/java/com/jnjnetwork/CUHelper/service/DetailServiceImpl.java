package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Detail;
import com.jnjnetwork.CUHelper.repository.BookRepository;
import com.jnjnetwork.CUHelper.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetailServiceImpl implements DetailService{

    DetailRepository detailRepository;
    BookRepository bookRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Autowired
    public void setDetailRepository(DetailRepository detailRepository) {
        this.detailRepository = detailRepository;
    }

    @Override
    public void add(Long book_id, Detail detail) {
        Book book = bookRepository.findById(book_id).orElseThrow(RuntimeException::new);
        detail.setBook(book);
        detailRepository.saveAndFlush(detail);
    }

    @Override
    public void deleteById(Long detail_id) {
        detailRepository.deleteById(detail_id);
    }

    @Override
    public List<Detail> findDetailByPage(Book book, Long firstPage, Long lastPage) {
        return detailRepository.listDetail(book, firstPage, lastPage);
    }
}
