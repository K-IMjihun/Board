package com.board.board.Entity;


import com.board.board.Dto.RequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String name;
    private String PW;
    private String contents;

    public Post(RequestDto requestDto) {
        this.id = requestDto.getId();
        this.title = requestDto.getTitle();
        this.name = requestDto.getName();
        this.PW = requestDto.getPW();
        this.contents = requestDto.getContents();

    }
}
