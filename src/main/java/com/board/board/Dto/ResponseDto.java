package com.board.board.Dto;

import com.board.board.Entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ResponseDto {
    private Long id;
    private String title;
    private String name;
    private String contents;

    public ResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.name = post.getName();
        this.contents = post.getContents();
    }
}


