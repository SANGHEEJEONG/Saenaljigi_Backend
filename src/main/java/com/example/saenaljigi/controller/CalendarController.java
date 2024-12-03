package com.example.saenaljigi.controller;

import com.example.saenaljigi.domain.User;
import com.example.saenaljigi.dto.CalendarDto;
import com.example.saenaljigi.service.CalendarService;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {
    private final CalendarService calendarService;
    @GetMapping("/day/user")
    public CalendarDto getCalendarByDateAndUser(@RequestParam("day")LocalDate day,
                                                @RequestParam Long userId){
        return calendarService.getCalendarByDateAndUser(day,userId);
    }
    @GetMapping("")
    public List<CalendarDto> getAllCalendarsByUser(@RequestParam Long userId){

        return calendarService.getAllCalendarsByUser(userId);
    }
    @PostMapping("/{calendarId}/breakfast")
    public ResponseEntity<Void> updateBreakfast(
            @PathVariable Long calendarId,
            @RequestParam Boolean isBreakfast) {
        calendarService.updateBreakfast(calendarId, isBreakfast);
        return ResponseEntity.ok().build();
    }




}
