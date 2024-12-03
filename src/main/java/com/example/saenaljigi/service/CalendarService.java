package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.domain.Food;
import com.example.saenaljigi.domain.Menu;
import com.example.saenaljigi.domain.User;
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



@Transactional(readOnly = true)
public List<CalendarDto> getAllCalendarsByUser(Long userId) {
    List<Calendar> calendars = calendarRepository.findAllByUserUserId(userId);

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
public CalendarDto getCalendarByDateAndUser(LocalDate day, Long userId) {
    Calendar calendar = calendarRepository.findByDayAndUserUserId(day, userId)
            .orElseThrow(() -> new RuntimeException("Calendar not found for date " + day + " and user " + userId));

    List<MenuDto> menus = menuService.getMenusByCalendarId(calendar.getId());

    return CalendarDto.builder()
            .id(calendar.getId())
            .day(calendar.getDay())
            .isHilight(calendar.getIsHilight())
            .isBreakfast(calendar.getIsBreakfast())
            .menus(menus)
            .build();
}

@Transactional
public void copySystemCalendarsToUser(User user) {
    // 시스템(사용자 없는) 캘린더를 모두 조회
    List<Calendar> systemCalendars = calendarRepository.findAllByUserUserId(null);

    for (Calendar systemCalendar : systemCalendars) {
        // 새로운 사용자용 캘린더 생성
        Calendar userCalendar = Calendar.builder()
                .day(systemCalendar.getDay())
                .user(user)
                .isHilight(systemCalendar.getIsHilight())
                .isBreakfast(systemCalendar.getIsBreakfast())
                .build();
        Calendar savedUserCalendar = calendarRepository.save(userCalendar);

        // 시스템 캘린더의 모든 메뉴를 조회
        List<Menu> systemMenus = menuRepository.findByCalendarId(systemCalendar.getId());

        for (Menu systemMenu : systemMenus) {
            // 새로운 사용자용 메뉴 생성
            Menu newMenu = Menu.builder()
                    .foodTime(systemMenu.getFoodTime())
                    .isCheck(systemMenu.isCheck())
                    .calendar(savedUserCalendar)
                    .build();
            Menu savedMenu = menuRepository.save(newMenu);

            // 시스템 메뉴의 모든 음식을 조회
            List<Food> systemFoods = foodRepository.findByMenuId(systemMenu.getId());

            for (Food systemFood : systemFoods) {
                // 새로운 사용자용 음식 생성
                Food newFood = Food.builder()
                        .foodName(systemFood.getFoodName())
                        .isSelected(systemFood.isSelected())
                        .menu(savedMenu)
                        .build();
                foodRepository.save(newFood);
            }
        }
    }
}

    // 기존 메서드: 사용자 없이 캘린더 조회 또는 생성
    @Transactional
    public Calendar getOrCreateSystemCalendarByDate(LocalDate date) {
        List<Calendar> calendars = calendarRepository.findAllByDayAndUserUserId(date, null);
        if (calendars.isEmpty()) {
            return calendarRepository.save(
                    Calendar.builder()
                            .day(date)
                            .user(null)
                            .build()
            );
        } else if (calendars.size() == 1) {
            return calendars.get(0);
        } else {
            // 중복된 시스템 캘린더가 존재할 경우, 첫 번째를 반환하거나 예외를 던짐
            // 여기서는 첫 번째를 반환하도록 함
            return calendars.get(0);
            // Alternatively, throw an exception
            // throw new NonUniqueResultException("Multiple system Calendars found for date: " + date);
        }
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
