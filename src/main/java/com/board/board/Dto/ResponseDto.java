package com.board.board.Dto;

import com.board.board.Entity.Post;
import lombok.Getter;

@Getter
public class ResponseDto {
    private Long id;
    private String title;
    private String name;
    private String PW;
    private String contents;

    public ResponseDto(Long id, String title, String name, String PW, String contents) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.PW = PW;
        this.contents = contents;
    }

    public ResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.name = post.getName();
        this.PW = post.getPW();
        this.contents = post.getContents();
    }
}


