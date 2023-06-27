package com.board.board.Entity;


import com.board.board.Dto.PostRequestDto;
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
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String PW;
    @Column(nullable = false, length = 500)
    private String contents;


    public Post(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.name = postRequestDto.getName();
        this.PW = postRequestDto.getPassword();
        this.contents = postRequestDto.getContents();
    }


}
