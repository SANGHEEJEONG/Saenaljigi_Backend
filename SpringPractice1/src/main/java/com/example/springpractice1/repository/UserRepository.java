package com.example.springpractice1.repository;

import com.example.springpractice1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
