package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
