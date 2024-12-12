package com.github.board.service.comment;

import com.github.board.repository.auth.user.Users;
import com.github.board.repository.comment.CommentJpaRepository;
import com.github.board.repository.comment.Comments;
import com.github.board.repository.post.Posts;
import com.github.board.service.exception.NotFoundException;
import com.github.board.web.dto.comment.Comment;
import com.github.board.web.dto.comment.Comment;
import com.github.board.web.dto.comment.CommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    //jpa repository
    private final CommentJpaRepository commentJpaRepository;
    private final PostRepository postRepository;

    /**
     * LocalDateTime 생성일을 "yyyy-MM-dd HH:mm:ss" 형식의 String으로 반환
     * */
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String localDateTimeToString(LocalDateTime localDateTime){
        if(localDateTime != null ) return localDateTime.format(formatter);
        else return null;
    }

    //게시글 idx 로 댓글불러오기
    public List<Comment> findCommentByPostIdx(Integer postIdx) {
        Posts post = postRepository.findById(postIdx.longValue())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 게시글 입니다."));

        List<Comments> comments =commentJpaRepository.findAllByPostsIdxOrderByCreatedAtAsc(postIdx);
        List<Comment> responseData= comments.stream().map(
                //coment 빌더 사용
                comment -> Comment.builder()
                        .id(comment.getIdx())
                        .postId(comment.getPostsIdx())
                        .author(comment.getUser().getUserName())
                        .content(comment.getBody())
                        .createdAt( localDateTimeToString(comment.getCreatedAt()) )
                        //   .authorIdx(comment.getUser().getUserId())
                        .build()
        ).collect(Collectors.toList());
        return responseData;
    }

    //댓글 등록
    public Comment registerComment(CommentRequest commentRequest) {
        Comments newComment = Comments.builder()
                .body(commentRequest.getContent())
                .postsIdx(  commentRequest.getPostId() )
                .user( Users.builder().userId(commentRequest.getUserId()).build() )
                .build();

        commentJpaRepository.save(newComment);

        Comments comments = commentJpaRepository.findByIdJoinUser(newComment.getIdx()).orElseThrow(
                () -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));


        Comment savecCmment = Comment.builder()
                .id(comments.getIdx())
                .postId(comments.getPostsIdx())
                .author(comments.getUser().getUserName())
                .content(comments.getBody())
                .createdAt( localDateTimeToString(comments.getCreatedAt()) )
                //   .authorIdx(comments.getUser().getUserId())
                .build();
        return savecCmment;
    }

    //댓글 수정
    @Transactional(transactionManager = "tmJpaComment")
    public void updateComment(String id, CommentRequest commentRequest) {
        Integer idx = Integer.valueOf(id);
        Comments comment = commentJpaRepository.findByIdJoinUser(idx).orElseThrow(
                () -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

        if(!comment.getUser().getUserId().equals(commentRequest.getUserId())) throw new AccessDeniedException("작성자 불일치 : 댓글 수정 권한이 없습니다.");
        comment.setBody(commentRequest.getContent());
    }


    //댓글 삭제
    public void deleteComment(String id, Integer userIdx) {
        Integer idx = Integer.valueOf(id);
        Comments comment = commentJpaRepository.findByIdJoinUser(idx).orElseThrow(
                () -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

        if(!comment.getUser().getUserId().equals(userIdx)) throw new AccessDeniedException("작성자 불일치 : 댓글 삭제 권한이 없습니다.");

        commentJpaRepository.delete(comment);
    }


}
