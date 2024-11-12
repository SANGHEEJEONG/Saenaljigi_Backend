package com.example.springpractice1.service;

import com.example.springpractice1.dto.UserDto;
import com.example.springpractice1.entity.User;
import com.example.springpractice1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //회원가입 로직
    public UserDto join(UserDto userDto){
        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setPassword(userDto.getPassword());
        newUser.setNickname(userDto.getNickname());

        userRepository.save(newUser);
        return userDto;
    }


}
