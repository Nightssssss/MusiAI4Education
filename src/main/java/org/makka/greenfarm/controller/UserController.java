package org.makka.greenfarm.controller;

import org.makka.greenfarm.domain.User;
import org.makka.greenfarm.service.UserService;
import org.makka.greenfarm.service.impl.UserServiceImpl;
import org.makka.greenfarm.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/accounts/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        // Perform authentication and get the authenticated username

        String token = JwtUtil.generateToken(username);
        System.out.println(token);
        return token;
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String token) {
        if (JwtUtil.validateToken(token)) {
            String username = JwtUtil.getUsernameFromToken(token);
            // Retrieve user information based on the username
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Perform user registration and account creation

        // Generate JWT token
        String token = JwtUtil.generateToken(user.getUsername());

        // Return the token to the frontend
        return ResponseEntity.ok(token);
    }
}

