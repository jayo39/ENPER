package com.jnjnetwork.CUHelper.service;

import com.jnjnetwork.CUHelper.domain.Role;
import com.jnjnetwork.CUHelper.domain.User;
import com.jnjnetwork.CUHelper.repository.HistoryRepository;
import com.jnjnetwork.CUHelper.repository.RoleRepository;
import com.jnjnetwork.CUHelper.repository.ScheduleRepository;
import com.jnjnetwork.CUHelper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    RoleRepository roleRepository;
    ScheduleRepository scheduleRepository;
    HistoryRepository historyRepository;

    @Autowired
    public void setScheduleRepository(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<Role> selectRolesById(Long id) {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return user.getRoles();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean isExist(String username) {
        User user = findByUsername(username);
        return user != null;
    }

    @Override
    public int register(User user) {
        try {
            user.setUsername(user.getUsername());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

            Role role = roleRepository.findByName("ROLE_MEMBER");

            user.addRole(role);
            userRepository.save(user); // update
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void removeById(Long user_id) {
        User user = userRepository.findById(user_id).orElseThrow(RuntimeException::new);
        scheduleRepository.deleteByUserId(user.getId());
        historyRepository.deleteByUserId(user.getId());
        userRepository.delete(user);
    }

    @Override
    public void updateLogDate(User user) {
        User u = userRepository.findById(user.getId()).orElseThrow(RuntimeException::new);
        if(u != null) {
            u.setLog_date(user.getLog_date());
            userRepository.saveAndFlush(u);
        }
    }
}
