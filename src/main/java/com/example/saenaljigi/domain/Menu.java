package com.example.saenaljigi.domain;

import com.example.saenaljigi.util.FoodTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name="menu",
        uniqueConstraints = @UniqueConstraint(columnNames = {"calendar_id", "food_time"})
)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Enumerated(EnumType.STRING)
    private FoodTime foodTime;
    private boolean isCheck;

    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    public void updateMenu(FoodTime foodTime, boolean isCheck) {
        this.foodTime = foodTime;
        this.isCheck = isCheck;
    }
}
