package com.ghostcoderz.blog_application.service.serviceInterface;

import com.ghostcoderz.blog_application.payload.PostDto;
import com.ghostcoderz.blog_application.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto,
                       Long userId,
                       Integer categoryId);
    PostDto updatePost(PostDto postDto, Long postId);
    PostDto getPostById(Long postId);

    PostResponse getAllPosts(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDir);

    void deletePost(Long postId);
    PostResponse getPostByCategory(
            Integer categoryId,
            Integer pageNumber,
            Integer pageSize);
    PostResponse getPostByUser(
            Long userId,
            Integer pageNumber,
            Integer pageSize
    );
    List<PostDto> searchPosts(String keyword);

}
