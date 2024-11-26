package com.example.saenaljigi.repository;

import com.example.saenaljigi.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food,Long> {
    Optional<Food> findByMenuIdAndName(Long menuId, String name);
    boolean existsByMenuIdAndIsSelected(Long menuId, boolean isSelected);
    List<Food> findByMenuId(Long menuId);
    void deleteByMenuId(Long menuId);
}
