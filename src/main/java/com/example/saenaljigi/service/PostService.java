package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Post;
import com.example.saenaljigi.domain.User;
import com.example.saenaljigi.dto.PostDto;
import com.example.saenaljigi.repository.PostRepository;
import com.example.saenaljigi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto create(Long userId,
                          PostDto postDto){
        //Id 찾기
        User user = userRepository.findById(userId).orElseThrow();
        //만든 유처 post에 집어넣기
        Post post = Post.builder().
                user(user).
                title(postDto.getTitle()).
                content(postDto.getContent()).
                build();
//        post.setUser(user);
//        post.setTitle(postDto.getTitle());
//        post.setContent(postDto.getContent());
        //레포지토리에 저장하기
        postRepository.save(post);

        return postDto;

    }
    //글 최신 순 글 조회

    public List<PostDto> getRecentPosts(){
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(PostDto::new)
                .collect(Collectors.toList());


    }

    //작성자 글 조회
    public List<PostDto> getPostsByWriter(String nickname){
        // 닉네임으로 찾고
        User user = userRepository.findByNickname(nickname);

        return postRepository.findAllByUserId(user.getId()).stream()
                .map(PostDto::new)
                .collect(Collectors.toList());




    }


    //댓글 많은 순 글 조회
    public List<PostDto> getPopularPosts(){
        return postRepository.findAllByOrderByCommentCntDesc().stream()
                .map(PostDto::new)
                .collect(Collectors.toList());


    }



}