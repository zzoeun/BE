package com.github.board.web.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    private String title;
    private String body;
    private String email;
    private Integer idx;
    private Integer userIdx;
}