package com.example.saenaljigi.controller;

import com.example.saenaljigi.domain.Post;
import com.example.saenaljigi.dto.PostDto;
import com.example.saenaljigi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {
    private final PostService postService;
    @PostMapping("")
    public ResponseEntity<Void> create(Long userId, @RequestParam("title")String title, @RequestParam("content")String content

    ){
        postService.create(userId, title, content);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public List<PostDto> getAllPosts(){

        return postService.getAllPosts();

    }
    //likecnt 업데이트
    @PostMapping("/like")
    public void updateLikeCnt( @RequestParam Long postId, @RequestParam Long likeCnt){

        postService.updateLikeCnt(postId,likeCnt);

    }

//    @GetMapping("/{writer}")
//    public List<PostDto> getPostsByWriter(@PathVariable("writer")String nickName){
//        return postService.getPostsByWriter(nickName);
//
//
//    }
//    @GetMapping("/comments")
//    public List<PostDto> getPopularPosts(){
//
//        return postService.getPopularPosts();
//    }
}
