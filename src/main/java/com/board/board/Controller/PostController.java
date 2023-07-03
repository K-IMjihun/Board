package com.board.board.Controller;

import com.board.board.Dto.PostDto.DeleteRequestDto;
import com.board.board.Dto.PostDto.DeleteResponseDto;
import com.board.board.Dto.PostDto.PostRequestDto;
import com.board.board.Dto.PostDto.PostResponseDto;
import com.board.board.Service.PostService;
import jakarta.servlet.ServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){

        this.postService = postService;
    }

    @PostMapping
    public PostResponseDto createBoard(ServletRequest request, @RequestBody PostRequestDto postRequestDto) {
        // RequestDto -> Entity
        return postService.createPost(request, postRequestDto);
    }



    //- 제목, 작성자명, 작성 내용, 작성 날짜를 조회하기
    //- 작성 날짜 기준 내림차순으로 정렬하기
    @GetMapping
    public List<PostResponseDto> getPosts() {
        // DB 조회
       return postService.getPosts();
    }
    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PutMapping("/{id}")
    public PostResponseDto updatePost(ServletRequest request, @PathVariable(value = "id") Long id, @RequestBody PostRequestDto postRequestDto) {
        // 해당 메모가 DB에 존재하는지 확인

        return postService.putPost(request, id, postRequestDto);
    }

    @DeleteMapping("/{id}")
    public DeleteResponseDto deleteMemo(ServletRequest request, @PathVariable Long id, @RequestBody DeleteRequestDto deleteRequestDto) {
        return postService.deletePost(request, id, deleteRequestDto);
    }

}
