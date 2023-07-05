package com.board.board.Dto;

import lombok.Data;

@Data

public class ResultResponseDto {
    String msg;
    Long statusCode;

    public ResultResponseDto(String msg, long statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
