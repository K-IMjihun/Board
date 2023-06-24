package com.board.board.Dto;
import com.board.board.Entity.Post;
import lombok.Getter;

@Getter
public class RequestDto {
    private Long id;
    private String title;
    private String name;
    private String PW;
    private String contents;


}