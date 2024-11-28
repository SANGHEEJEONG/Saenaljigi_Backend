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
                .id(calendar.getId())
                .day(calendar.getDay())
                .isHilight(calendar.getIsHilight())
                .isBreakfast(calendar.getIsBreakfast())
                .menus(menus)
                .build();
    }
    @Transactional
    public void createDefaultCalendarsForUser(User user) {
        // 예시로 기본적인 캘린더 데이터 (일년 중 몇 개의 날짜들에 대한 캘린더)
        for (int i = 1; i <= 7; i++) {  // 일주일 간의 캘린더 예시
            LocalDate date = LocalDate.now().plusDays(i);
            Calendar calendar = Calendar.builder()
                    .day(date)
                    .user(user)
                    .build();
            calendarRepository.save(calendar);
        }
    }
//user별로 가져오도록 수정해야함
    @Transactional
    public Calendar getOrCreateCalendarByDate(LocalDate date) {
        return calendarRepository.findByDay(date)
                .orElseGet(() -> calendarRepository.save(
                        Calendar.builder()
                                .day(date)
                                .user(null)
                                .build()
                ));
    }
    @Transactional
    public Calendar getOrCreateUserCalendarByDate(LocalDate date, User user) {
        return calendarRepository.findByDayAndUser(date, user)
                .orElseGet(() -> calendarRepository.save(
                        Calendar.builder()
                                .day(date)
                                .user(user)
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
