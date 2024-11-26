package com.example.saenaljigi.dto;

import com.example.saenaljigi.domain.Comment;
import com.example.saenaljigi.domain.Post;
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
        //this.userId = post.getUser().get();
        // userId => 테이블 키
        // username => 학번
        // name => 이름

        this.title = post.getTitle();
        this.content = post.getContent();
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
    private Long likeCnt;
    private Long commentCnt;
    private List<CommentDto> comments;
    private LocalDateTime createdAt;
}
