package com.github.board.service.post;

import com.github.board.repository.auth.user.Users;
import com.github.board.repository.post.PostRepository;
import com.github.board.repository.post.Posts;
import com.github.board.service.exception.InvalidValueException;
import com.github.board.service.exception.NotFoundException;
import com.github.board.web.dto.post.PostRequest;
import com.github.board.web.dto.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String localDateTimeToString(LocalDateTime localDateTime){
        if(localDateTime != null ) return localDateTime.format(formatter);
        else return null;
    }

    // 게시물 전체 조회
    public List<PostResponse> findAllPost() {
        List <Posts> post = postRepository.findAllPost();
        if(post.isEmpty()){
            throw new NotFoundException("게시글이 없습니다.");
        }

        List<PostResponse> responseData = post.stream().map(
                        posts -> PostResponse.builder()
                                .idx(posts.getIdx())
                                .title(posts.getTitle())
                                .body(posts.getBody())
                                .author(posts.getUser().getUserName())
                                .created_at(localDateTimeToString(posts.getCreated_at()))
                                .email(posts.getUser().getEmail())
                                .build())
                .collect(Collectors.toList());
        return responseData;
    }

    //     게시물 생성
    public void register(PostRequest postRequest) {

        Posts posts = Posts.builder()
                .title(postRequest.getTitle())
                .body(postRequest.getBody())
                .user(Users.builder().userId(postRequest.getUserIdx()).build())
                .build();

        Posts savedPost = postRepository.save(posts);
    }

    // 이메일로 게시물 조회
    public List<PostResponse> getPostByEmail(String email) {
        List <Posts> post = postRepository.findByUserEmail(email);
        if(post.isEmpty()){
            throw new InvalidValueException("해당 email로 조회된 게시글이 없습니다.");
        }

        List<PostResponse> responseData = post.stream().map(
                        posts -> PostResponse.builder()
                                .idx(posts.getIdx())
                                .title(posts.getTitle())
                                .body(posts.getBody())
                                .created_at(localDateTimeToString(posts.getCreated_at()))
                                .email(posts.getUser().getEmail())
                                .author(posts.getUser().getUserName())
                                .build())
                .collect(Collectors.toList());
        return responseData;
    }

    // 게시물 수정
    public PostResponse updateBodyByIdx(PostRequest postRequest) {
        Posts post = postRepository.findByIdx(postRequest.getIdx())
                .orElseThrow(() -> new InvalidValueException("해당 idx의 게시글이 없습니다."));
        if(!post.getUser().getUserId().equals(postRequest.getUserIdx())) {
            throw new AccessDeniedException("작성자 불일치 : 게시글 수정 권한이 없습니다."); }
        post.setBody(postRequest.getBody());
        post.setTitle(postRequest.getTitle());
        Posts updatedPost = postRepository.save(post);


        return new PostResponse(
                updatedPost.getIdx(),
                updatedPost.getTitle(),
                updatedPost.getBody(),
                updatedPost.getUser().getEmail(),
                updatedPost.getUser().getUserName(),
                updatedPost.getCreated_at().toString()
        );
    }

    //idx로 게시물 삭제
    public PostResponse deleteByIdx(Integer idx, Integer userId) {
        Posts post = postRepository.findByIdx(idx)
                .orElseThrow(() -> new InvalidValueException("삭제할 게시글이 없습니다."));
        if(!post.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("작성자 불일치 : 게시글 삭제 권한이 없습니다."); }
        postRepository.delete(post);
        return null;
    }
}



