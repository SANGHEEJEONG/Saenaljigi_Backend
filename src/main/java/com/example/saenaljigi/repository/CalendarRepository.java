package com.example.saenaljigi.repository;

import com.example.saenaljigi.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    Optional<Calendar> findByDay(LocalDate day);
}
