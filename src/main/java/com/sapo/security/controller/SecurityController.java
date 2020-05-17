package com.sapo.security.controller;

import com.sapo.security.model.JwtResponse;
import com.sapo.security.model.Role;
import com.sapo.security.model.User;
import com.sapo.security.service.JwtService;
import com.sapo.security.service.RoleService;
import com.sapo.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class SecurityController {
    private static final String DEFAULT_ROLE = "ROLE_USER";
    private static final String DEFAULT_ADMIN = "ROLE_ADMIN";


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired

    private JwtService jwtService;
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<Iterable<User>> showAllUser() {
        Iterable<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {

//        List<Role> roleList = (List<Role>)roleService.findAll();
////        if (roleList.isEmpty()){
////            Role roleAdmin = new Role();
////            Role roleUser = new Role();
////            roleAdmin.setId(1L);
////            roleUser.setId(2L);
////            roleAdmin.setName("ROLE_ADMIN");;
////            roleUser.setName("ROLE_USER");
////            roleService.save(roleAdmin);
////            roleService.save(roleUser);
////
////        }
////        List<User> userList = (List<User>) userService.findAll();
////        if(userList.isEmpty()){
////            User admin = new User();
////            admin.setId(1L);
////            admin.setUsername("admin");
////            admin.setPassword(passwordEncoder.encode("12345"));
////            Role role = roleService.findRoleByName(DEFAULT_ADMIN);
////            Set<Role> roles = new HashSet<>();
////            roles.add(role);
////            admin.setRoles(roles);
////            userService.save(admin);
////        }

        Iterable<User> users = userService.findAll();
        for (User currentUser : users) {
            if (currentUser.getUsername().equals(user.getUsername())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }


        Role role = roleService.findRoleByName(DEFAULT_ROLE);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateTokenLogin(authentication);
        //xac thuc
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }
}
