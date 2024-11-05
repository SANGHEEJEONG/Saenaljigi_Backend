package com.example.saenaljigi.dto;

import com.example.saenaljigi.domain.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class CalendarDto {
    private LocalDate day;
    private Boolean isHilight;
    private Boolean isBreakfast;
    private List<MenuDto> menus;
}
