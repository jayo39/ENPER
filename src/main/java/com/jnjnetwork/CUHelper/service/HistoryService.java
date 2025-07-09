package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.History;

import java.util.List;

public interface HistoryService {
    List<History> findAll();
    List<History> findByUserId(Long user_id);
    void save(History history);
    void deleteById(Long id);
    void deleteByBookId(Long book_id);
    void deleteAll();
}
