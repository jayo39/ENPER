package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Role;
import com.jnjnetwork.CUHelper.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByUsername(String username);
    Optional<User> findById(Long id);
    List<Role> selectRolesById(Long id);
    List<User> findAll();
    boolean isExist(String username);
    int register(User user);
    void removeById(Long user_id);
    void updateLogDate(User user);
    boolean toggleFavorite(Long userId, Long bookId);
}
