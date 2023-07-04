package com.board.board.Controller.PostController;

import com.board.board.Dto.PostDto.PostResponseDto;
import com.board.board.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cm/post")
public class PostCommonController {

    private final PostService postService;
    @GetMapping
    public List<PostResponseDto> getPosts() {
        // DB 조회
        return postService.getPosts();
    }
    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

}
