package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.User;

public interface UserService {
    User findByUsername(String username);
}
