package com.board.board.Service;

import com.board.board.Dto.DeleteResponseDto;
import com.board.board.Dto.PasswordRequestDto;
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


    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    @Transactional
    public PostResponseDto putPost(Long id, PostRequestDto requestDto) {
//          해당 메모가 DB에 존재하는지 확인
            Post post = findPost(id);
            PostResponseDto postResponseDto;

        // 비밀번호 확인
            if(post.getPW().equals(requestDto.getPassword())) {
                post.update(requestDto);

                postResponseDto = new PostResponseDto(post);
            }
            else{
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }
            return postResponseDto;
       }

    public DeleteResponseDto deletePost(Long id, PasswordRequestDto passwordRequestDto) {
        // 해당 메모가 DB에 존재하는지 확인
            Post post = findPost(id);
            DeleteResponseDto deleteResponseDto = new DeleteResponseDto();

        // 비밀번호 확인
            if(post.getPW().equals(passwordRequestDto.getPassword())) {
                postRepository.delete(post);
                deleteResponseDto.setSuccess(true);
            }
            else{
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }
            return deleteResponseDto;
        }




    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글이 존재하지 않습니다."));
    }
}
