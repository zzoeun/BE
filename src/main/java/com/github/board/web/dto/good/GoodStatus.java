package com.github.board.web.dto.good;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GoodStatus {

    ALREADY_LIKED("이미 해당 게시글에 좋아요를 누르셨습니다.", 400),
    LIKED("해당 게시글을 좋아합니다.", 200),
    ALREADY_UNLIKED("해당 게시글에 취소할 좋아요가 존재하지 않습니다.", 400),
    UNLIKED("해당 게시글에 좋아요가 취소되었습니다.", 200);

    private final String message;
    private final int code;

}
