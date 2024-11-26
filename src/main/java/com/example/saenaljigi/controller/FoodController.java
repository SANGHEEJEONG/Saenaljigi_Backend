package com.example.saenaljigi.controller;

import com.example.saenaljigi.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
    @PutMapping("food/{foodId}/select")
    public ResponseEntity<Void> updateFoodSelection(
            @PathVariable Long foodId,
            @RequestParam boolean isSelected) {
        foodService.updateFoodSelection(foodId, isSelected);
        return ResponseEntity.ok().build();
    }
}
