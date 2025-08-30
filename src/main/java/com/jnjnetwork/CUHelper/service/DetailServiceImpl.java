package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Detail;
import com.jnjnetwork.CUHelper.repository.BookRepository;
import com.jnjnetwork.CUHelper.repository.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void add(Long book_id, Detail detail) {
        Book book = bookRepository.findById(book_id).orElseThrow(RuntimeException::new);
        detail.setBook(book);
        detailRepository.saveAndFlush(detail);
    }

    @Override
    @Transactional
    public void deleteById(Long detail_id) {
        detailRepository.deleteById(detail_id);
    }

    @Override
    public List<Detail> findDetailByPage(Book book, Long firstPage, Long lastPage) {
        Sort sort = Sort.by(Sort.Order.asc("firstPage"));
        return detailRepository.listDetail(book, firstPage, lastPage, sort);
    }

    @Override
    public List<Detail> findByBook(Book book) {
        if (book != null) {
            Long book_id = book.getId();
            Sort sort = Sort.by(Sort.Order.asc("firstPage"));
            return detailRepository.findByBookId(book_id, sort);
        } else {
            return null;
        }
    }

    @Override
    public Detail findById(Long detail_id) {
        return detailRepository.findById(detail_id).orElseThrow(RuntimeException::new);
    }

    @Override
    @Transactional
    public void edit(Detail detail, Book book) {
        Detail newDetail = detailRepository.findById(detail.getId()).orElseThrow(RuntimeException::new);
        newDetail.setDetail(detail.getDetail());
        newDetail.setBook(book);
        newDetail.setFirstPage(detail.getFirstPage());
        newDetail.setLastPage(detail.getLastPage());
        detailRepository.saveAndFlush(newDetail);
    }

    @Override
    @Transactional
    public void deleteByBookId(Long book_id) {
        detailRepository.deleteByBookId(book_id);
    }
}
