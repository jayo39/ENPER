package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Book;
import com.jnjnetwork.CUHelper.domain.Question;
import com.jnjnetwork.CUHelper.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService{

    QuestionRepository questionRepository;

    @Autowired
    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    @Override
    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    public Question findByBookId(Long book_id) {
        return questionRepository.findByBookId(book_id);
    }

    @Override
    public void add(Question question) {
        questionRepository.save(question);
    }

    @Override
    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public void edit(Long id, Book book, String content) {
        Question question = questionRepository.findById(id).orElseThrow(RuntimeException::new);
        question.setContent(content);
        question.setBook(book);
        questionRepository.save(question);
    }
}
