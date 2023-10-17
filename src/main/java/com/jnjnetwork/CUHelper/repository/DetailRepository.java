package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Detail;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, Long> {
    @Query("SELECT DISTINCT d FROM Detail d WHERE (:page1 <= d.lastPage AND :page2 >= d.firstPage) AND book = :book")
    List<Detail> listDetail(@Param("book") Book book, @Param("page1") Long firstPage, @Param("page2") Long lastPage, Sort sort);
    List<Detail> findByBookId(Long book_id, Sort sort);
    void deleteByBookId(Long book_id);
}
