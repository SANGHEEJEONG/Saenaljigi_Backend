package com.example.saenaljigi.dto;

public class UserResponse {
    private Long studentId;
    private String name;
    private Long mealCnt;
    private Long reward;
    private Long penalty;
    private Long totalCnt;

    public UserResponse(Long studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }
}
