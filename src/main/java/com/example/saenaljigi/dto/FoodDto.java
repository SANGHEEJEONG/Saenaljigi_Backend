package com.example.saenaljigi.dto;

import com.example.saenaljigi.domain.Menu;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodDto {
    private String foodName;
    private boolean isSelected;
    private Long menuId;
}
