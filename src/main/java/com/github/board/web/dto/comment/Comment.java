package com.github.board.web.dto.comment;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Comment {
    private Integer id;
    private String content; //댓글 내용
    private String author; // 작성자
    private Integer postId; //게시글 번호
    private String createdAt; //작성일시
//    private Integer authorIdx;

}
