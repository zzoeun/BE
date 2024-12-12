package com.github.board.web.dto.comment;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponse extends CommentDefaultResponse {

    private List<Comment> comments;

}
