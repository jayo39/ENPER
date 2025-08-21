package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT e FROM Book e WHERE e.titleNormalized LIKE CONCAT('%', :processedKeyword, '%') OR e.seriesNormalized LIKE CONCAT('%', :processedKeyword, '%')")
    List<Book> findByNormalizedKeyword(@Param("processedKeyword") String processedKeyword, Sort sort);
}
