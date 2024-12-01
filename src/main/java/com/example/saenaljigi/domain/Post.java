package com.example.saenaljigi.domain;

import com.example.saenaljigi.dto.CommentDto;
import com.example.saenaljigi.dto.UserRequest;
import com.example.saenaljigi.dto.UserResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private String title;
    private String content;
    private Long likeCnt;
    private Long commentCnt;
    private LocalDateTime createdAt;
    public void updateLikeCnt(Long likeCnt) {
        this.likeCnt = likeCnt;
    }
    public void updatecommentCnt(Long commentCnt) {
        this.commentCnt = commentCnt;
    }
}
