package com.ghostcoderz.blog_application.controller;

import com.ghostcoderz.blog_application.payload.ApiResponse;
import com.ghostcoderz.blog_application.payload.PostDto;
import com.ghostcoderz.blog_application.payload.PostResponse;
import com.ghostcoderz.blog_application.service.serviceInterface.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Validated
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostDto postDto,
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "categoryId") Integer categoryId
    ){
        PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Long userId){
        List<PostDto> postsByUser = this.postService.getPostByUser(userId);
        return new ResponseEntity<>(postsByUser, HttpStatus.OK);
    }

    @GetMapping("category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId){
        List<PostDto> postsByCategory = this.postService.getPostByCategory(categoryId);
        return new ResponseEntity<>(postsByCategory, HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(
                    value = "page",
                    defaultValue = "0",
                    required = false
            )Integer pageNumber,
            @RequestParam(
                    value = "pageSize",
                    defaultValue = "10",
                    required = false
            ) Integer pageSize
    ){
        PostResponse allPosts = this.postService.
                getAllPosts(pageNumber, pageSize);
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById( @PathVariable Long postId){
        PostDto post = this.postService.getPostById(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId){
        this.postService.deletePost(postId);
        return new ResponseEntity<>(
                new ApiResponse("Post is successfully deleted", true),
                HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> updatePost(
            @RequestBody PostDto postDto,
            @PathVariable Long postId){
        this.postService.updatePost(postDto, postId);
        return new ResponseEntity<>(
                new ApiResponse(
                        "Post is updated successfully",
                        true),
                HttpStatus.OK);
    }

}
