package com.github.board.web.controller.post;

import com.github.board.service.exception.CAuthenticationEntryPointException;
import com.github.board.service.post.PostService;
import com.github.board.web.dto.post.PostRequest;
import com.github.board.web.dto.post.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.github.board.repository.auth.userDetails.CustomUserDetails;

import java.util.List;
import java.util.Map;

@Slf4j //log를 가져오기 위해 사용
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostController {

    private final PostService postService;

    // 게시물 전체 조회
    @GetMapping("/post") // HTTP GET 요청 처리
    public ResponseEntity<List<PostResponse>> findAllPost() {
        log.info("GET /api/posts 게시물 전체 조회 요청이 들어왔습니다.");
        List<PostResponse> posts = postService.findAllPost();
        log.info(posts.toString());
        return ResponseEntity.ok(posts);
    }

    // 게시물 생성
    @PostMapping("/post/write") // 게시물 전체 조회와 엔드포인트가 같아서 수정
    public ResponseEntity<String> createPost(@RequestBody PostRequest request,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("POST /api/posts/new 게시물 생성 요청이 들어왔습니다: {}", request);

        if(userDetails == null){
            throw new CAuthenticationEntryPointException("로그인 정보가 아닙니다.");
        }

        String email = userDetails.getEmail();
        Integer userId = userDetails.getUserId();
        request.setUserIdx(userId);
        postService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("게시물이 성공적으로 작성되었습니다.");
    }

    //게시물 이메일로 조회
    @GetMapping("/post/email")
    //파라미터 ?author_email=user@example.com
    public ResponseEntity read(@RequestParam("author_email") String email) {
        log.info("GET /api/posts/search 게시물 이메일 조회 요청이 들어왔습니다.", email);
        List<PostResponse> postResponse = postService.getPostByEmail(email);
        return ResponseEntity.ok(postResponse);
    }

    // idx로 게시물 수정
    @PutMapping("/post/edit")
    public ResponseEntity<String> updatePostByIdx(@RequestBody Map<String, String> request,
                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null){
            throw new CAuthenticationEntryPointException("로그인 정보가 아닙니다.");
        }

        Integer userId = userDetails.getUserId();
        Integer idx = Integer.valueOf(request.get("idx"));
        String body = request.get("body");
        String title = request.get("title");
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle(title);
        postRequest.setBody(body);
        postRequest.setIdx(idx);
        postRequest.setUserIdx(userId);
        PostResponse updatedPost = postService.updateBodyByIdx(postRequest);
        return ResponseEntity.ok("게시물이 성공적으로 수정되었습니다.");
    }

    //title로 게시물 삭제
    @DeleteMapping("post/delete")
    public ResponseEntity<String> deleteByIdx(@RequestBody Map<String, Integer> request,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer idx = request.get("idx");
        log.info("DELETE /api/posts/delete 게시물 삭제 요청이 들어왔습니다: idx={}", idx);

        if(userDetails == null){
            throw new CAuthenticationEntryPointException("로그인 정보가 아닙니다.");
        }

        Integer userId = userDetails.getUserId();
        PostResponse deletePost = postService.deleteByIdx(idx, userId);
        return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
    }
}



