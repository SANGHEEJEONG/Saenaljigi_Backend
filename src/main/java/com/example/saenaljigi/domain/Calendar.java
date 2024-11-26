package com.example.saenaljigi.domain;


import jakarta.persistence.*;
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
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    public void updateHilight(boolean isHilight) {
        this.isHilight = isHilight;
    }
    public void updateBreakfast(boolean isBreakfast){
        this.isBreakfast = isBreakfast;


    }
}
