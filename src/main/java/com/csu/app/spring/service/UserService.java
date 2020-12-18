package com.csu.app.spring.service;

import com.csu.app.spring.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    User updateUser(User user);

    List<User> getAll();

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);
}
