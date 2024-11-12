package com.example.springpractice1.service;

import com.example.springpractice1.entity.Post;
import com.example.springpractice1.entity.User;
import com.example.springpractice1.repository.PostRepository;
import com.example.springpractice1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 작성 로직
    public Post createPost(Long userId, String title, String content){
        User user = userRepository.findById(userId).orElseThrow();
        Post post = new Post();
        post.setUser(user);
        post.setTitle(title);
        post.setContent(content);

        return postRepository.save(post);
    }
}
