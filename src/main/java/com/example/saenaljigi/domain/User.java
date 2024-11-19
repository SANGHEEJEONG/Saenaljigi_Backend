package com.example.saenaljigi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long studentId;
    private String name;
    private Long mealCnt;
    private Long reward;
    private Long penalty;
    private Long totalCnt;

    public User(Long studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }
}
