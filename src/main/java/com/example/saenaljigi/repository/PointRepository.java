package com.example.saenaljigi.repository;


import com.example.saenaljigi.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point,Long> {
}
