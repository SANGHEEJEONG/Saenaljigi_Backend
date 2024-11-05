package com.example.saenaljigi.repository;
import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.domain.Menu;
import com.example.saenaljigi.util.FoodTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public interface MenuRepository extends JpaRepository<Menu,Long>{
    boolean existsByCalendarAndFoodTime(Calendar calendar, FoodTime foodTime);

    List<Menu> findByCalendarId(Long calendarId);



}
