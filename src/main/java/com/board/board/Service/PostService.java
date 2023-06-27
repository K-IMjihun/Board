package com.board.board.Service;

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
        Post savePost = postRepository.save(post);

        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }


    public List<PostResponseDto> getPost() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public Long putPost(Long id, PostRequestDto requestDto) {
        //해당 메모가 DB에 존재하는지 확인
        Post post = findPost(id);

        post.update(requestDto);

        return id;
    }

    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("선택한 게시글이 존재하지 않습니다."));
    }
}
