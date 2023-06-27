package com.board.board.Service;

import com.board.board.Dto.PasswordDto;
import com.board.board.Dto.PostRequestDto;
import com.board.board.Entity.Post;
import com.board.board.Repository.PostRepository;
import com.board.board.Dto.PostResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);

        postRepository.save(post);

        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }


    public List<PostResponseDto> getPost() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public Long putPost(Long id, PostRequestDto requestDto) {
//          해당 메모가 DB에 존재하는지 확인
            Post post = findPost(id);

            // 비밀번호 확인
            if(post.getPW().equals(requestDto.getPassword())) {
                post.update(requestDto);
            }
            else{
                throw new IllegalArgumentException("비밀번호가 틀립니다");
            }
            return id;
       }

    public Long deletePost(Long id, PasswordDto passwordDto) {
        // 해당 메모가 DB에 존재하는지 확인
            Post post = findPost(id);

        // 비밀번호 확인
            if(post.getPW().equals(passwordDto.getPassword())) {
                postRepository.delete(post);
            }
            else{
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }
            return id;
        }



    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("선택한 게시글이 존재하지 않습니다."));
    }
}
