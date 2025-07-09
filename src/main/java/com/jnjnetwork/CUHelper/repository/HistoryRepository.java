package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.History;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUserId(Long user_id, Sort sort);
    void deleteByUserId(Long user_id);
    void deleteByBookId(Long book_id);
}
