package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Question;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface QuestionService {
    List<Question> findAllSortedByBookArPoint();
    Question findById(Long id);
    Question findByBookId(Long book_id);
    void add(Question question);
    void add(Question question, MultipartFile file);
    void deleteById(Long id, String fileName);
    void edit(Long id, Book book, String content);
}
