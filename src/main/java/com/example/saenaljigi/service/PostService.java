package com.example.saenaljigi.service;

import com.example.saenaljigi.domain.Comment;
import com.example.saenaljigi.domain.Post;
import com.example.saenaljigi.domain.User;
import com.example.saenaljigi.dto.PostDto;
import com.example.saenaljigi.repository.CommentRepository;
import com.example.saenaljigi.repository.PostRepository;
import com.example.saenaljigi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ResponseEntity<Void> create(Long userId,
                                 String title,
                                 String content){
        //Id 찾기
        User user = userRepository.findById(userId).orElseThrow();
        //만든 유처 post에 집어넣기
        Post post = Post.builder().
                user(user).
                createdAt(LocalDateTime.now()).
                title(title).
                content(content).
                build();

        //레포지토리에 저장하기
        postRepository.save(post);

        return ResponseEntity.ok().build();

    }
//
    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();

        // 각 Post에 대해 관련 댓글 조회
        return posts.stream()
                .map(post -> {
                    List<Comment> comments = commentRepository.findByPostId(post.getId());
                    return new PostDto(post, comments);
                })
                .collect(Collectors.toList());
    }
    //글 최신 순 글 조회

//    public List<PostDto> getRecentPosts(){
//        return postRepository.findAllByOrderByCreatedAtDesc()
//                .stream()
//                .map(PostDto::new)
//                .collect(Collectors.toList());
//
//
//    }




//    //댓글 많은 순 글 조회
//    public List<PostDto> getPopularPosts(){
//        return postRepository.findAllByOrderByCommentCntDesc().stream()
//                .map(PostDto::new)
//                .collect(Collectors.toList());
//
//
//    }



}
