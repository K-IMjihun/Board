package com.board.board.Entity;


import com.board.board.Dto.PostDto.PostRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String username;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 500)
    private String contents;


    public Post(PostRequestDto postRequestDto, String username) {
        this.title = postRequestDto.getTitle();
        this.contents = postRequestDto.getContents();
        this.username = username;
    }

    public void update(PostRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

}
