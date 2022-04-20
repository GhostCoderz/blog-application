package com.ghostcoderz.blog_application.service.serviceInterface;

import com.ghostcoderz.blog_application.payload.PostDto;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto,
                       Long userId,
                       Integer categoryId);
    PostDto updatePost(PostDto postDto, Long postId);
    PostDto getPostById(Long postId);
    List<PostDto> getAllPosts();
    void deletePost(Long postId);
    List<PostDto> getPostByCategory(Integer categoryId);
    List<PostDto> getPostByUser(Long userId);
    List<PostDto> searchPosts(String keyword);

}
