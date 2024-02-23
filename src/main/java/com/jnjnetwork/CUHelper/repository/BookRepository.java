package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT e FROM Book e WHERE e.title LIKE %:keyword% OR e.series LIKE %:keyword%")
    List<Book> findByKeywordInColumns(@Param("keyword") String keyword, Sort sort);
}
