package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.domain.Food;
import com.example.saenaljigi.domain.Menu;
import com.example.saenaljigi.repository.CalendarRepository;
import com.example.saenaljigi.repository.FoodRepository;
import com.example.saenaljigi.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final MenuRepository menuRepository;
    private final CalendarRepository calendarRepository;

    @Transactional
    public void updateFoodSelection(Long foodId, boolean isSelected) {
        // 1. Food 엔티티 조회 및 업데이트
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food not found with id: " + foodId));

        food.updateSelected(isSelected);
        foodRepository.save(food);

        // 2. 관련 Menu 엔티티 조회 및 상태 업데이트
        Menu menu = food.getMenu();
        Long menuId = menu.getId();
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
