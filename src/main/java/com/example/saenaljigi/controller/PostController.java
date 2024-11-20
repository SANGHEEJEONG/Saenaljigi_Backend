package com.example.saenaljigi.controller;

import com.example.saenaljigi.dto.PostDto;
import com.example.saenaljigi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping("")
    public PostDto create(Long userId,
                          @ModelAttribute PostDto postDto){
        return postService.create(userId,postDto);
    }

    @GetMapping("")
    public List<PostDto> getAllPosts(){

        return postService.getAllPosts();

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
