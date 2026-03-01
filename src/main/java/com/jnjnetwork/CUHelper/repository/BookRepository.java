package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b " +
            "ORDER BY (SELECT COUNT(u) FROM b.favoritedByUsers u WHERE u.id = :userId) DESC, b.title ASC")
    Page<Book> findAllFavoritesFirst(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT b FROM Book b " +
            "WHERE b.titleNormalized LIKE CONCAT('%', :keyword, '%') " +
            "OR b.seriesNormalized LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY (SELECT COUNT(u) FROM b.favoritedByUsers u WHERE u.id = :userId) DESC, b.title ASC")
    List<Book> findByKeywordFavoritesFirst(@Param("keyword") String keyword, @Param("userId") Long userId);
}
