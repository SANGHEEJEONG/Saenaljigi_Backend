package com.example.springpractice1.controller;

import com.example.springpractice1.dto.UserDto;
import com.example.springpractice1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public UserDto join(@ModelAttribute UserDto userDto){
        return userService.join(userDto);
    }

}
