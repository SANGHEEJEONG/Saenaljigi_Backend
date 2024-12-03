package com.example.saenaljigi.controller;

import com.example.saenaljigi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    @PostMapping("")
    public ResponseEntity<Void> create(Long userId, Long postId,@RequestParam("content")String content, @RequestParam("isAnonymous") boolean isAnonymous

    ){
        commentService.create(userId,postId, content,isAnonymous);
        return ResponseEntity.ok().build();
    }

}
