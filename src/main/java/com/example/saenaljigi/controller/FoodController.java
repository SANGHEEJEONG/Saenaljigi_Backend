package com.example.saenaljigi.controller;

import com.example.saenaljigi.service.FoodService;
import com.example.saenaljigi.util.FoodTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("food")
public class FoodController {
    private final FoodService foodService;
    @PutMapping("/select")
    public ResponseEntity<Void> updateFoodSelection(
            @RequestParam("menuId") Long menuId,       // menuId로 변경
            @RequestParam("isSelected") boolean isSelected,
            @RequestParam("foodname") String foodname) {
        foodService.updateFoodSelection(menuId, isSelected, foodname);
        return ResponseEntity.ok().build();
    }
}
