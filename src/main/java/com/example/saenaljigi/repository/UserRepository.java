package com.example.saenaljigi.repository;

import com.example.saenaljigi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByNickname(String nickname);

}
