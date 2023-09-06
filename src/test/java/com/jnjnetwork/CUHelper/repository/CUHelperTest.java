package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Role;
import com.jnjnetwork.CUHelper.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
public class CUHelperTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void init() {
        Role member = Role.builder()
                .name("ROLE_MEMBER")
                .build();
        Role admin = Role.builder()
                .name("ROLE_ADMIN")
                .build();

        roleRepository.saveAndFlush(member);
        roleRepository.saveAndFlush(admin);

        User user = User.builder()
                .username("jayo39")
                .password(passwordEncoder.encode("1111"))
                .build();

        user.addRole(member, admin);
        userRepository.saveAndFlush(user);
    }
}
