package com.board.board.Service;

import com.board.board.Dto.ResultResponseDto;
import com.board.board.Dto.PostDto.PostRequestDto;
import com.board.board.Dto.PostDto.PostResponseDto;
import com.board.board.Entity.Post;
import com.board.board.Jwt.JwtUtil;
import com.board.board.Repository.PostRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    public PostResponseDto createPost(ServletRequest request, PostRequestDto postRequestDto)  {
        String username = userCheck(request);
        Post post = new Post(postRequestDto, username);

        postRepository.save(post);

        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    @Transactional
    public PostResponseDto putPost(ServletRequest request, Long id, PostRequestDto requestDto) {
            Post post = findPost(id);
            String username = userCheck(request);
            PostResponseDto postResponseDto;

            if(post.getUsername().equals(username)) {
                post.update(requestDto);
                postResponseDto = new PostResponseDto(post);
            }
            else{
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }
            return postResponseDto;
       }

    public ResultResponseDto deletePost(ServletRequest request, Long id) {
        Post post = findPost(id);
        String username = userCheck(request);

        if(post.getUsername().equals(username)) {
            postRepository.delete(post);
        }
        else{
            throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
        }
        ResultResponseDto resultResponseDto = new ResultResponseDto("게시글 삭제 성공", 200L);
        return resultResponseDto;
    }

    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글이 존재하지 않습니다."));
    }

    private String userCheck(ServletRequest request){
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        String username = info.getSubject();

        return username;
    }
}
