package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByBookId(Long book_id);
    @Query("SELECT q FROM Question q LEFT JOIN q.book b ORDER BY b.ar_point ASC")
    List<Question> findAllSortedByBookArPoint();
}
