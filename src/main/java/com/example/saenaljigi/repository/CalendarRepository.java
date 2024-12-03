package com.example.saenaljigi.repository;

import com.example.saenaljigi.domain.Calendar;
import com.example.saenaljigi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    List<Calendar> findAllByUserUserId(Long userId);
    List<Calendar> findAllByDayAndUserUserId(LocalDate day,Long userId);

    Optional<Calendar> findByDayAndUserUserId(LocalDate day, Long userId);
}
