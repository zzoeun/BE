package com.github.board.web.dto.good;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoodResult {

    private String msg;
    private int code;

    public GoodResult(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
}
