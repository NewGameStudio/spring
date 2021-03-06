package com.csu.app.spring.rest;

import com.csu.app.spring.dto.AuthenticationRequestDto;
import com.csu.app.spring.model.User;
import com.csu.app.spring.security.jwt.JwtTokenProvider;
import com.csu.app.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping(value = "register")
    public ResponseEntity register(@RequestBody AuthenticationRequestDto requestDto) {

        System.out.println("Register");

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        userService.register(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {

        try {

            System.out.println("Authorization");

            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));

            User user = userService.findByUsername(username);

            if(user == null)
                throw new UsernameNotFoundException("User with username : " + username + " not found");

            String token = jwtTokenProvider.createToken(username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
