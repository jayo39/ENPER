package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Role;
import com.jnjnetwork.CUHelper.domain.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    List<Role> selectRolesById(Long id);
    List<User> findAll();
    boolean isExist(String username);
    int register(User user);
    void removeById(Long user_id);
}
