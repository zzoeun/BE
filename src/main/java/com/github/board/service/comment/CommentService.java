package com.github.board.service.comment;

import com.github.board.repository.comment.CommentJpaRepository;
import com.github.board.repository.comment.Comments;
import com.github.board.repository.post.Posts;
import com.github.board.service.exception.NotFoundException;
import com.github.board.web.dto.comment.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
