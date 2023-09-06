package com.jnjnetwork.CUHelper.repository;

import com.jnjnetwork.CUHelper.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
