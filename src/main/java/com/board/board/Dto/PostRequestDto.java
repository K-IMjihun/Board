package com.board.board.Dto;
import lombok.Data;

@Data
public class PostRequestDto {
    private String title;
    private String name;
    private String password;
    private String contents;
}