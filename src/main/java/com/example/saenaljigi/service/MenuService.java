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
import com.example.saenaljigi.util.FoodTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final CalendarRepository calendarRepository;
    private final FoodRepository foodRepository;
    public boolean existsByCalendarAndFoodTime(Calendar calendar, FoodTime foodTime) {
        return menuRepository.existsByCalendarAndFoodTime(calendar, foodTime);
    }


    @Transactional
    public MenuDto createMenu(MenuDto menuDto) {
        // Calendar 조회 또는 예외 발생
        Calendar calendar = calendarRepository.findById(menuDto.getCalendarId())
                .orElseThrow(() -> new RuntimeException("Calendar not found with id " + menuDto.getCalendarId()));

        // Menu 엔티티 생성
        Menu menu = Menu.builder()
                .foodTime(Enum.valueOf(com.example.saenaljigi.util.FoodTime.class, menuDto.getFoodTime()))
                .isCheck(menuDto.isCheck())
                .calendar(calendar)
                .build();
        Menu savedMenu = menuRepository.save(menu);

        // Food 엔티티 생성 및 저장
        if (menuDto.getFoods() != null && !menuDto.getFoods().isEmpty()) {
            List<Food> foods = menuDto.getFoods().stream()
                    .map(foodDto -> {
                        Food food = Food.builder()
                                .foodName(foodDto.getFoodName())
                                .isSelected(foodDto.isSelected())
                                .menu(savedMenu)
                                .build();
                        return food;
                    })
                    .collect(Collectors.toList());
            foodRepository.saveAll(foods);
        }

        // 저장된 Menu를 MenuDto로 변환하여 반환
        return convertToDto(savedMenu);
    }


    @Transactional(readOnly = true)
    public MenuDto getMenuById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id " + id));
        return convertToDto(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuDto> getMenusByCalendarId(Long calendarId) {
        List<Menu> menus = menuRepository.findByCalendarId(calendarId);
        return menus.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public MenuDto updateMenu(Long id, MenuDto menuDto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id " + id));

        menu.updateMenu(Enum.valueOf(com.example.saenaljigi.util.FoodTime.class, menuDto.getFoodTime()), menuDto.isCheck());

        // 기존 Foods 삭제 및 새로운 Foods 저장
        foodRepository.deleteByMenuId(id);
        if (menuDto.getFoods() != null && !menuDto.getFoods().isEmpty()) {
            List<Food> foods = menuDto.getFoods().stream()
                    .map(foodDto -> Food.builder()
                            .foodName(foodDto.getFoodName())
                            .isSelected(foodDto.isSelected())
                            .menu(menu)
                            .build())
                    .collect(Collectors.toList());
            foodRepository.saveAll(foods);
        }

        // 업데이트된 Menu를 MenuDto로 변환하여 반환
        return convertToDto(menu);
    }
    @Transactional
    public void deleteMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id " + id));
        foodRepository.deleteByMenuId(id);
        menuRepository.delete(menu);
    }

    @Transactional(readOnly = true)
    public List<CalendarDto> getAllCalendarsWithMenus() {
        List<Calendar> calendars = calendarRepository.findAll();
        return calendars.stream()
                .map(calendar -> {
                    List<MenuDto> menus = getMenusByCalendarId(calendar.getId());
                    return CalendarDto.builder()
//                            .id(calendar.getId())
                            .day(calendar.getDay())
                            .isHilight(calendar.getIsHilight())
                            .isBreakfast(calendar.getIsBreakfast())
                            .menus(menus)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private MenuDto convertToDto(Menu menu) {
        // 관련된 Food 엔티티를 조회하여 FoodDto 리스트 생성
        List<FoodDto> foodDtos = foodRepository.findByMenuId(menu.getId()).stream()
                .map(food -> FoodDto.builder()
                        .foodName(food.getFoodName())
                        .isSelected(food.isSelected())
                        .menuId(food.getMenu() != null ? food.getMenu().getId() : null)
                        .build())
                .collect(Collectors.toList());

        return MenuDto.builder()
                .id(menu.getId())
                .foodTime(menu.getFoodTime().name())
                .isCheck(menu.isCheck())
                .foods(foodDtos)
                .calendarId(menu.getCalendar() != null ? menu.getCalendar().getId() : null)
                .build();
    }
}
