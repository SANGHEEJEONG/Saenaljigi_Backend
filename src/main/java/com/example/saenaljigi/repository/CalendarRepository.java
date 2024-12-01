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
    List<Calendar> findAllByUser(User user);
    List<Calendar> findAllByDayAndUser(LocalDate day,User user);
    Optional<Calendar> findByDay(LocalDate day);
    List<Calendar> findByUser(User user);
    Optional<Calendar> findByDayAndUser(LocalDate day, User user);
}
