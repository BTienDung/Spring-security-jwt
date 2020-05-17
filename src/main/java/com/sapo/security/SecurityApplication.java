package com.sapo.security;


import com.sapo.security.model.Role;
import com.sapo.security.model.User;
import com.sapo.security.repository.UserRepository;
import com.sapo.security.service.RoleService;
import com.sapo.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableJpaRepositories("com.sapo.security")
@EntityScan("com.sapo.security")
public class SecurityApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Override
    public void run(String... args) throws Exception {
        // Khi chương trình chạy
        // Insert vào csdl một user.
        List<Role> roleList = (List<Role>)roleService.findAll();
        if (roleList.isEmpty()){
            Role roleAdmin = new Role();
            Role roleUser = new Role();
            roleAdmin.setId(1L);
            roleUser.setId(2L);
            roleAdmin.setName("ROLE_ADMIN");;
            roleUser.setName("ROLE_USER");
            roleService.save(roleAdmin);
            roleService.save(roleUser);

        }
        List<User> userList = (List<User>) userService.findAll();
        if(userList.isEmpty()){
            User admin = new User();
            admin.setId(1L);
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("12345"));
            Role role = roleService.findRoleByName("ROLE_ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            admin.setRoles(roles);
            userService.save(admin);
        }
    }
}
