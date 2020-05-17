package com.sapo.security.service;

import com.sapo.security.model.Role;

public interface RoleService {
    Role findRoleByName(String roleName);
    Iterable<Role> findAll();
    void save (Role role);
}
