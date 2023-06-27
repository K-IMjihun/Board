package com.board.board.Service;

import com.board.board.Dto.PostRequestDto;
import com.board.board.Entity.Post;
import com.board.board.Repository.PostRepository;
import com.board.board.Dto.PostResponseDto;
import org.springframework.stereotype.Service;

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
}
