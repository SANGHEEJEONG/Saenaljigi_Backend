package com.example.saenaljigi.controller;

import com.example.saenaljigi.dto.MenuDto;
import com.example.saenaljigi.service.MenuService;
import com.example.saenaljigi.util.FoodTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

//    @GetMapping("/{day}/{foodtime}")
//    public List<MenuDto> findByFoodTimeAndCalendar(@PathVariable("day")LocalDate day, @PathVariable("foodtime")FoodTime foodTime){
//        return menuService.findByFoodTimeAndCalendar(foodTime,day);
//
//    }


}
