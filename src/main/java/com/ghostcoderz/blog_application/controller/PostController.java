package com.ghostcoderz.blog_application.controller;

import com.ghostcoderz.blog_application.config.AppConstants;
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
    public ResponseEntity<PostResponse> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(
                    value = "page",
                    defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,
                    required = false
            )Integer pageNumber,
            @RequestParam(
                    value = "pageSize",
                    defaultValue = AppConstants.DEFAULT_PAGE_SIZE,
                    required = false
            ) Integer pageSize
    ){
        PostResponse postsByUser = this.postService.
                getPostByUser(userId, pageNumber, pageSize);
        return new ResponseEntity<>(postsByUser, HttpStatus.OK);
    }

    @GetMapping("category/{categoryId}/posts")
    public ResponseEntity<PostResponse> getPostsByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(
                    value = "page",
                    defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,
                    required = false
            )Integer pageNumber,
            @RequestParam(
                    value = "pageSize",
                    defaultValue = AppConstants.DEFAULT_PAGE_SIZE,
                    required = false
            ) Integer pageSize
    ){
        PostResponse postsByCategory = this.postService.
                getPostByCategory(categoryId, pageNumber, pageSize);

        return new ResponseEntity<>(postsByCategory, HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(
                    value = "page",
                    defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,
                    required = false
            )Integer pageNumber,
            @RequestParam(
                    value = "pageSize",
                    defaultValue = AppConstants.DEFAULT_PAGE_SIZE,
                    required = false
            ) Integer pageSize,
            @RequestParam(
                    value = "sortBy",
                    defaultValue = AppConstants.DEFAULT_SORT_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    value = "sortDir",
                    defaultValue = AppConstants.DEFAULT_SORT_DIR,
                    required = false
            ) String sortDir
    ){
        PostResponse allPosts = this.postService.
                getAllPosts(pageNumber, pageSize, sortBy, sortDir);
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

    @GetMapping("/posts/search/{keywords}")
    public ResponseEntity<List<PostDto>> searchByTitle(
            @PathVariable("keywords") String keywords
    ){
        List<PostDto> result = this.postService.searchPosts(keywords);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
