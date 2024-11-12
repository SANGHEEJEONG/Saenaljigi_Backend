package com.example.saenaljigi.repository;

import com.example.saenaljigi.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {
    List<Food> findByMenuId(Long menuId);
    void deleteByMenuId(Long menuId);
}
