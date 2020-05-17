package com.sapo.security.repository;

import com.sapo.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByName(String name);
}
