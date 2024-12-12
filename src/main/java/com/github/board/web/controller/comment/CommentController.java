package com.github.board.web.controller.comment;

import com.github.board.service.comment.CommentService;
import com.github.board.service.exception.InvalidValueException;
import com.github.board.web.dto.comment.Comment;
import com.github.board.web.dto.comment.CommentListResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
