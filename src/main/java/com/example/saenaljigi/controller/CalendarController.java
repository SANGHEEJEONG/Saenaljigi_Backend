package com.example.saenaljigi.controller;

import com.example.saenaljigi.domain.User;
import com.example.saenaljigi.dto.CalendarDto;
import com.example.saenaljigi.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    @GetMapping("/day/user")
    public CalendarDto getCalendarByDateAndUser(@RequestParam("day")LocalDate day, User user){
        return calendarService.getCalendarByDateAndUser(day,user);
    }
    @GetMapping("")
    public List<CalendarDto> getAllCalendarsByUser(User user){

        return calendarService.getAllCalendarsByUser(user);
    }
    @PostMapping("/{calendarId}/breakfast")
    public ResponseEntity<Void> updateBreakfast(
            @PathVariable Long calendarId,
            @RequestParam Boolean isBreakfast) {
        calendarService.updateBreakfast(calendarId, isBreakfast);
        return ResponseEntity.ok().build();
    }




}
