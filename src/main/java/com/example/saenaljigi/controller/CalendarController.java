package com.example.saenaljigi.controller;

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

    @GetMapping("/day")
    public CalendarDto getCalendarByDate(@RequestParam("day")LocalDate day){

        return calendarService.getCalendarByDate(day);
    }
    @GetMapping("")
    public List<CalendarDto> getAllCalendars(){

        return calendarService.getAllCalendars();
    }
    @PostMapping("/{calendarId}/breakfast")
    public ResponseEntity<Void> updateBreakfast(
            @PathVariable Long calendarId,
            @RequestParam Boolean isBreakfast) {
        calendarService.updateBreakfast(calendarId, isBreakfast);
        return ResponseEntity.ok().build();
    }


}
