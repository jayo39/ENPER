package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.History;
import com.jnjnetwork.CUHelper.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService{

    HistoryRepository historyRepository;

    @Autowired
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<History> findAll() {
        return historyRepository.findAll(Sort.by(Sort.Order.desc("accessDate")));
    }

    @Override
    public List<History> findByUserId(Long user_id) {
        Sort sort = Sort.by(Sort.Order.desc("accessDate"));
        return historyRepository.findByUserId(user_id, sort);
    }

    @Override
    public void save(History history) {
        historyRepository.saveAndFlush(history);
    }

    @Override
    public void deleteById(Long id) {
        historyRepository.deleteById(id);
    }

    @Override
    public void deleteByBookId(Long book_id) {
        historyRepository.deleteByBookId(book_id);
    }

    @Override
    public void deleteAll() {
        historyRepository.deleteAll();
    }
}
