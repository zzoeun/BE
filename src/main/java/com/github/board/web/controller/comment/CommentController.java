package com.github.board.web.controller.comment;

import com.github.board.repository.auth.userDetails.CustomUserDetails;
import com.github.board.service.comment.CommentService;
import com.github.board.service.exception.CAuthenticationEntryPointException;
import com.github.board.service.exception.InvalidValueException;
import com.github.board.web.dto.comment.Comment;
import com.github.board.web.dto.comment.CommentRequest;
import com.github.board.web.dto.comment.CommentResponse;
import com.github.board.web.dto.comment.CommentDefaultResponse;
import com.github.board.web.dto.comment.CommentListResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;


    /**
     * 게시글 idx를 통해 해당 게시글의 전체 댓글을 조회
     * Get /api/comment/post-comments
     * 필요 파라미터 : Integer idx
     * 리턴타입 : ResponseEntity<CommentListResponse>
     * */
    @GetMapping("/post-comments")
    public ResponseEntity<CommentListResponse> findCommentByPostIdx(HttpServletRequest request) throws IOException {
        Integer postIdx;
        try{
            postIdx = Integer.valueOf( request.getParameter("idx") );
        }catch (Exception e){
            throw new InvalidValueException("(댓글 조회)잘못된 요청입니다. 입력된 idx: "+request.getParameter("idx"));
        }
        List<Comment> comments = commentService.findCommentByPostIdx(postIdx);

        String responseMessage = postIdx+"번 게시글의 댓글목록입니다.("+comments.size()+")";
        CommentListResponse commentListResponse = new CommentListResponse();
        commentListResponse.setComments(comments);
        commentListResponse.setMessage(responseMessage);
        commentListResponse.setStatus(HttpStatus.OK.value());

        // ResponseEntity
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<CommentListResponse> (commentListResponse, headers, HttpStatus.OK);

    }

    /**
     * 댓글 작성
     * Post /api/comment/write
     * 필요 파라미터 : CommentRequest("content": "추가하는 내용","post_id": 게시글 idx)
     * 로그인확인 처리
     * 리턴타입 : ResponseEntity<CommentListResponse>
     * */
    @PostMapping("/write")
    public ResponseEntity<CommentResponse> registerComment(@RequestBody CommentRequest commentRequest,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails,
                                                           HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

        if(userDetails == null) throw new CAuthenticationEntryPointException("로그인 후 댓글을 작성할 수 있습니다.");
        Integer userIdx = userDetails.getUserId();
        commentRequest.setUserId(userIdx);
        Comment comment = commentService.registerComment(commentRequest);


        String responseMessage = "댓글이 성공적으로 작성되었습니다.";
        // ResponseEntity
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setMessage(responseMessage);
        commentResponse.setStatus(HttpStatus.OK.value());
        commentResponse.setComment(comment);

        return new ResponseEntity<CommentResponse> (commentResponse, headers, HttpStatus.OK);
    }


    /**
     * 댓글 수정
     * Put /api/comment/edit/{id}
     * 필요 파라미터 : CommentRequest("content": "수정된 댓글 내용"),@PathVariable  String id
     * 로그인확인 처리
     * 리턴타입 : ResponseEntity<CommentDefaultResponse>
     * */
    @PutMapping("/edit/{id}")
    public ResponseEntity<CommentDefaultResponse> updateComment(@PathVariable String id,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @RequestBody CommentRequest commentRequest){

        if(userDetails == null) throw new CAuthenticationEntryPointException("로그인 후 댓글을 수정할 수 있습니다.");
        Integer userIdx = userDetails.getUserId();
        commentRequest.setUserId(userIdx);

        commentService.updateComment(id, commentRequest);


        String responseMessage = id + "번 댓글이 성공적으로 수정되었습니다.";

        // ResponseEntity
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        CommentDefaultResponse commentResponse = new CommentDefaultResponse();
        commentResponse.setMessage(responseMessage);
        commentResponse.setStatus(HttpStatus.OK.value());

        return new ResponseEntity<CommentDefaultResponse> (commentResponse, headers, HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     * Delete /api/comment/delete/{id}
     * 필요 파라미터 : @PathVariable String id
     * 로그인확인 처리
     * 리턴타입 : ResponseEntity<CommentDefaultResponse>
     * */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommentDefaultResponse> deleteComment(@PathVariable String id, @AuthenticationPrincipal CustomUserDetails userDetails){
        if(userDetails == null) throw new CAuthenticationEntryPointException("로그인 후 댓글을 삭제할 수 있습니다.");
        Integer userIdx = userDetails.getUserId();

        commentService.deleteComment(id, userIdx);

        String responseMessage = id + "번 댓글이 성공적으로 삭제되었습니다.";

        // ResponseEntity
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        CommentDefaultResponse commentResponse = new CommentDefaultResponse();
        commentResponse.setMessage(responseMessage);
        commentResponse.setStatus(HttpStatus.OK.value());

        return new ResponseEntity<CommentDefaultResponse> (commentResponse, headers, HttpStatus.OK);
    }




}
