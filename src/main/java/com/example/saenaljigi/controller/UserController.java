package com.example.saenaljigi.controller;

import com.example.saenaljigi.dto.UserRequest;
import com.example.saenaljigi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserRequest userRequest) {
        String loginUrl = "https://happydorm.sejong.ac.kr/00/0000_login.kmc";

        return userService.login(loginUrl, userRequest);
    }
}

