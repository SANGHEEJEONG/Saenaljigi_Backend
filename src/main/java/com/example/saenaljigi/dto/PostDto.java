package com.example.saenaljigi.dto;

import com.example.saenaljigi.domain.Comment;
import com.example.saenaljigi.domain.Post;
import com.example.saenaljigi.domain.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class PostDto {
    public PostDto(Post post,List<Comment> comments) {
        this.id = post.getId();
       this.userId = post.getUser().getStudentId();

        this.title = post.getTitle();
        this.content = post.getContent();
        this.anonymousName = post.getAnonymousName();
        this.likeCnt = post.getLikeCnt();
        this.commentCnt = post.getCommentCnt();
        this.comments = comments.stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());


        this.createdAt = post.getCreatedAt();

    }


    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String anonymousName;
    private Long likeCnt;
    private Long commentCnt;
    private List<CommentDto> comments;
    private LocalDateTime createdAt;
}
