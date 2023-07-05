package org.makka.greenfarm.controller;

import org.makka.greenfarm.utils.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {
    @PostMapping("/generateToken")
    public String generateToken(@RequestBody String username) {
        String token = JwtUtil.generateToken(username);
        return token;
    }
}

