package com.example.saenaljigi.controller;

import com.example.saenaljigi.domain.User;
import com.example.saenaljigi.dto.UserRequest;
import com.example.saenaljigi.dto.UserResponse;
import com.example.saenaljigi.jwt.JWTUtil;
import com.example.saenaljigi.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public UserResponse login(@RequestBody UserRequest userRequest, HttpServletResponse response) throws LoginException {
        String loginUrl = "https://happydorm.sejong.ac.kr/00/0000_login.kmc";

        User user = userService.isLoginSuccess(loginUrl, userRequest);

        String token = jwtUtil.createAccessToken(userRequest.getUsername());

        response.addHeader("Authorization", "Bearer " + token);

        return new UserResponse(user.getUserId(), user.getUsername(), user.getMealCnt());
    }
}

