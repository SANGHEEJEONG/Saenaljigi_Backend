package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.dto.CalendarDto;
import com.example.saenaljigi.dto.MenuDto;
import com.example.saenaljigi.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final MenuService menuService;


    @Transactional(readOnly = true)
    public List<CalendarDto> getAllCalendarsWithMenus() {
        List<Calendar> calendars = calendarRepository.findAll();
        return calendars.stream()
                .map(calendar -> {
                    List<MenuDto> menus = menuService.getMenusByCalendarId(calendar.getId());
                    return CalendarDto.builder()
                            .day(calendar.getDay())
                            .isHilight(calendar.getIsHilight())
                            .isBreakfast(calendar.getIsBreakfast())
                            .menus(menus)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CalendarDto getCalendarWithMenus(Long calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found with id " + calendarId));

        // 관련된 메뉴 목록을 조회하여 MenuDto 리스트로 변환
        List<MenuDto> menus = menuService.getMenusByCalendarId(calendar.getId());

        return CalendarDto.builder()
                .day(calendar.getDay())
                .isHilight(calendar.getIsHilight())
                .isBreakfast(calendar.getIsBreakfast())
                .menus(menus)
                .build();
    }

    @Transactional(readOnly = true)
    public CalendarDto getCalendarByDate(LocalDate date) {
        Calendar calendar = calendarRepository.findByDay(date)
                .orElseThrow(() -> new RuntimeException("Calendar not found for date " + date));

        List<MenuDto> menus = menuService.getMenusByCalendarId(calendar.getId());

        return CalendarDto.builder()
                .day(calendar.getDay())
                .isHilight(calendar.getIsHilight())
                .isBreakfast(calendar.getIsBreakfast())
                .menus(menus)
                .build();
    }
    @Transactional(readOnly = true)
    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }
    @Transactional
    public Calendar getOrCreateCalendarByDate(LocalDate date) {
        return calendarRepository.findByDay(date)
                .orElseGet(() -> calendarRepository.save(
                        Calendar.builder()
                                .day(date)
                                .isHilight(false)
                                .isBreakfast(false)
                                .build()
                ));
    }



}
