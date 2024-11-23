package com.example.saenaljigi.dto;

import com.example.saenaljigi.domain.Comment;
import com.example.saenaljigi.domain.Post;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {
    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.anonymousName = comment.isAnonymousName();
        this.replyCnt = comment.getReplyCnt();
        this.createdAt = comment.getCreatedAt();

    }
    private Long id;
    private Long postId;
    private String content;
    private boolean anonymousName;
    private Integer replyCnt;
    private LocalDateTime createdAt;
}
