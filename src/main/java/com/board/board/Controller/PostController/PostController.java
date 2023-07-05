package com.board.board.Controller.PostController;

import com.board.board.Dto.ResultResponseDto;
import com.board.board.Dto.PostDto.PostRequestDto;
import com.board.board.Dto.PostDto.PostResponseDto;
import com.board.board.Service.PostService;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public PostResponseDto createPost(ServletRequest request, @RequestBody PostRequestDto postRequestDto) {

        return postService.createPost(request, postRequestDto);
    }

    @PutMapping("/{id}")
    public PostResponseDto updatePost(ServletRequest request, @PathVariable(value = "id") Long id, @RequestBody PostRequestDto postRequestDto) {

        return postService.putPost(request, id, postRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResultResponseDto deletePost(ServletRequest request, @PathVariable Long id) {

        return postService.deletePost(request, id);
    }

}
