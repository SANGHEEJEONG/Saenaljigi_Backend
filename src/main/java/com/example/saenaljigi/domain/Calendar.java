package com.example.saenaljigi.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="calendar")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate day;
    private Boolean isHilight;
    private Boolean isBreakfast;
    public void updateHilight(boolean isHilight) {
        this.isHilight = isHilight;
    }
    public void updateBreakfast(boolean isBreakfast){
        this.isBreakfast = isBreakfast;


    }
}
