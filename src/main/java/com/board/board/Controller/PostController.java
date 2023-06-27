package com.board.board.Controller;


import com.board.board.Dto.PostRequestDto;
import com.board.board.Dto.PostResponseDto;
import com.board.board.Entity.Post;
import com.board.board.Service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    public PostResponseDto createBoard(@RequestBody PostRequestDto postRequestDto) {
        // RequestDto -> Entity
        return postService.createPost(postRequestDto);
    }



    //- 제목, 작성자명, 작성 내용, 작성 날짜를 조회하기
    //- 작성 날짜 기준 내림차순으로 정렬하기
    @GetMapping
    public List<PostResponseDto> getPosts() {
        // DB 조회
       return postService.getPost();
    }

    @PutMapping("/{id}")
    public Long updatePost(@PathVariable(value = "id") Long id, @RequestBody PostRequestDto postRequestDto) {
        // 해당 메모가 DB에 존재하는지 확인

        return postService.putPost(id, postRequestDto);
    }

    @DeleteMapping("/{id}")
    public Long deleteMemo(@PathVariable Long id) {

        return null;
    }

    private Post findById(Long id) {
        return null;
        // DB 조회
    }
}
