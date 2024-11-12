package com.example.saenaljigi.dto;

import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.util.FoodTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
public class MenuDto {

    private Long id;
    private String foodTime; // "LUNCH" 또는 "DINNER"
    private boolean isCheck;
    private List<FoodDto> foods;
    private Long calendarId;
}
