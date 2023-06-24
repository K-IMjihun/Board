package com.board.board.Dto;
import com.board.board.Entity.Post;
import lombok.Data;
import lombok.Getter;

@Data
public class RequestDto {
    private String title;
    private String name;
    private String password;
    private String contents;


}