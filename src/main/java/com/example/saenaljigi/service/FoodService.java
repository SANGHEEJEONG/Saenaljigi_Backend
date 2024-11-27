package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.domain.Food;
import com.example.saenaljigi.domain.Menu;
import com.example.saenaljigi.repository.CalendarRepository;
import com.example.saenaljigi.repository.FoodRepository;
import com.example.saenaljigi.repository.MenuRepository;
import com.example.saenaljigi.util.FoodTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final MenuRepository menuRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public void updateFoodSelection(Long menuId, boolean isSelected, String foodname) {
        // 1. menuId와 foodname을 기준으로 Food 엔티티 조회
        Food food = foodRepository.findByMenuIdAndFoodName(menuId, foodname)
                .orElseThrow(() -> new RuntimeException(
                        "Food not found with menuId: " + menuId + ", foodname: " + foodname));

        // 2. 엔티티의 선택 상태 업데이트
        food.updateSelected(isSelected);
        foodRepository.save(food);

        // 2. 관련 Menu 엔티티 조회 및 상태 업데이트
        Menu menu = food.getMenu();
        Long calendarId = menu.getCalendar().getId();

        // 메뉴 내에 하나 이상의 Food가 선택되었는지 확인
        boolean anySelectedInMenu = foodRepository.existsByMenuIdAndIsSelected(menuId, true);
        menu.updateCheck(anySelectedInMenu);
        menuRepository.save(menu);

        // 3. 관련 Calendar 엔티티 조회 및 상태 업데이트
        // 캘린더 내에 하나 이상의 Menu가 체크되었는지 확인
        boolean anyCheckInCalendar = menuRepository.existsByCalendarIdAndIsCheck(calendarId, true);
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found with id: " + calendarId));
        calendar.updateHilight(anyCheckInCalendar);
        calendarRepository.save(calendar);
    }



}
