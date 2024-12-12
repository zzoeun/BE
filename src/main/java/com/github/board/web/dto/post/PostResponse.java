package com.github.board.web.dto.post;

import lombok.*;
import lombok.Getter;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer idx;
    private String title;
    private String body;
    private String email;
    private String author;
    private String created_at;
}