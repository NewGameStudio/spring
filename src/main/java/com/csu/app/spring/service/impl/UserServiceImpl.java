package com.csu.app.spring.service.impl;

import com.csu.app.spring.model.Role;
import com.csu.app.spring.model.User;
import com.csu.app.spring.repository.RoleRepository;
import com.csu.app.spring.repository.UserRepository;
import com.csu.app.spring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {

        Role roleUser = roleRepository.findByName("ROLE_USER");

        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        User registeredUser = userRepository.save(user);

        System.out.println("UserServiceImpl::register");

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();

        System.out.println("UserServiceImpl::getAll");

        return users;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);

        System.out.println("UserServiceImpl::findByUsername");

        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null) {
            System.out.println("UserServiceImpl::findById, no user found : " + id);
            return null;
        }

        System.out.println("UserServiceImpl::findById");

        return user;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        System.out.println("UserServiceImpl::delete");
    }
}
