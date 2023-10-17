package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByBookId(Long book_id);
}
