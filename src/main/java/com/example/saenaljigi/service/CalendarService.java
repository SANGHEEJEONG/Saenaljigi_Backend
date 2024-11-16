package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.domain.Food;
import com.example.saenaljigi.domain.Menu;
import com.example.saenaljigi.dto.CalendarDto;
import com.example.saenaljigi.dto.FoodDto;
import com.example.saenaljigi.dto.MenuDto;
import com.example.saenaljigi.repository.CalendarRepository;
import com.example.saenaljigi.repository.FoodRepository;
import com.example.saenaljigi.repository.MenuRepository;
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
    private final MenuRepository menuRepository;
    private final FoodRepository foodRepository;


//    @Transactional(readOnly = true)
//    public List<CalendarDto> getAllCalendarsWithMenus() {
//        List<Calendar> calendars = calendarRepository.findAll();
//        return calendars.stream()
//                .map(calendar -> {
//                    List<MenuDto> menus = menuService.getMenusByCalendarId(calendar.getId());
//                    return CalendarDto.builder()
//                            .day(calendar.getDay())
//                            .isHilight(calendar.getIsHilight())
//                            .isBreakfast(calendar.getIsBreakfast())
//                            .menus(menus)
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }

//    @Transactional(readOnly = true)
//    public CalendarDto getCalendarWithMenus(Long calendarId) {
//        Calendar calendar = calendarRepository.findById(calendarId)
//                .orElseThrow(() -> new RuntimeException("Calendar not found with id " + calendarId));
//
//        // 관련된 메뉴 목록을 조회하여 MenuDto 리스트로 변환
//        List<MenuDto> menus = menuService.getMenusByCalendarId(calendar.getId());
//
//        return CalendarDto.builder()
//                .day(calendar.getDay())
//                .isHilight(calendar.getIsHilight())
//                .isBreakfast(calendar.getIsBreakfast())
//                .menus(menus)
//                .build();
//    }
@Transactional(readOnly = true)
public List<CalendarDto> getAllCalendars() {
    List<Calendar> calendars = calendarRepository.findAll();

    return calendars.stream()
            .map(calendar -> {
                // 각 Calendar에 해당하는 Menu 리스트 조회
                List<Menu> menus = menuRepository.findByCalendarId(calendar.getId());

                // Menu를 MenuDto로 변환
                List<MenuDto> menuDtos = menus.stream()
                        .map(menu -> {
                            // 각 Menu에 해당하는 Food 리스트 조회
                            List<Food> foods = foodRepository.findByMenuId(menu.getId());

                            // Food를 FoodDto로 변환
                            List<FoodDto> foodDtos = foods.stream()
                                    .map(food -> FoodDto.builder()
                                            .foodName(food.getFoodName())
                                            .isSelected(food.isSelected())
                                            .menuId(menu.getId())
                                            .build())
                                    .collect(Collectors.toList());

                            // MenuDto 생성
                            return MenuDto.builder()
                                    .id(menu.getId())
                                    .foodTime(menu.getFoodTime().name())
                                    .isCheck(menu.isCheck())
                                    .calendarId(calendar.getId())
                                    .foods(foodDtos)
                                    .build();
                        })
                        .collect(Collectors.toList());

                // CalendarDto 생성
                return CalendarDto.builder()
                        .id(calendar.getId())
                        .day(calendar.getDay())
                        .isHilight(calendar.getIsHilight())
                        .isBreakfast(calendar.getIsBreakfast())
                        .menus(menuDtos)
                        .build();
            })
            .collect(Collectors.toList());
}

    @Transactional(readOnly = true)
    public CalendarDto getCalendarByDate(LocalDate day) {
        Calendar calendar = calendarRepository.findByDay(day)
                .orElseThrow(() -> new RuntimeException("Calendar not found for date " + day));

        List<MenuDto> menus = menuService.getMenusByCalendarId(calendar.getId());

        return CalendarDto.builder()
                .day(calendar.getDay())
                .isHilight(calendar.getIsHilight())
                .isBreakfast(calendar.getIsBreakfast())
                .menus(menus)
                .build();
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

    @Transactional
    public void updateBreakfast(Long calendarId, Boolean isBreakfast) {
        // 1. Calendar 엔티티 조회
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow();

        // 2. isBreakfast 필드 수정
        calendar.updateBreakfast(isBreakfast);

        // 3. 변경된 Calendar 저장 (JPA는 변경 감지로 자동 저장)
        // Optional: 명시적으로 저장할 필요는 없지만, 코드의 명확성을 위해 저장할 수 있습니다.
        calendarRepository.save(calendar);
    }




}
