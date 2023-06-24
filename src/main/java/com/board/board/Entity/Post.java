package com.board.board.Entity;


import com.board.board.Dto.RequestDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Post {
    private Long id;
    private String title;
    private String name;
    private String PW;
    private String contents;

    public Post(RequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.name = requestDto.getName();
        this.PW = requestDto.getPassword();
        this.contents = requestDto.getContents();

    }

    public Post() {

    }
}
