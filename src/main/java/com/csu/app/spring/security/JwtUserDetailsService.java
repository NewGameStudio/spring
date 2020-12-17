package com.csu.app.spring.security;

import com.csu.app.spring.model.User;
import com.csu.app.spring.security.jwt.JwtUser;
import com.csu.app.spring.security.jwt.JwtUserFactory;
import com.csu.app.spring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if(user == null)
            throw new UsernameNotFoundException("User with username : " + username + " not found");

        JwtUser jwtUser = JwtUserFactory.create(user);

        System.out.println("JwtUserDetailsService::loadUserByUsername");

        return jwtUser;
    }
}
