package com.example.saenaljigi.repository;

import com.example.saenaljigi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {

    User findByStudentId(Long userId);
}
