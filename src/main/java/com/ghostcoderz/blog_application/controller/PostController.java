package com.ghostcoderz.blog_application.controller;

import com.ghostcoderz.blog_application.config.AppConstants;
import com.ghostcoderz.blog_application.payload.ApiResponse;
import com.ghostcoderz.blog_application.payload.PostDto;
import com.ghostcoderz.blog_application.payload.PostResponse;
import com.ghostcoderz.blog_application.service.serviceImpl.FileServiceImpl;
import com.ghostcoderz.blog_application.service.serviceInterface.PostService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1")
@CrossOrigin("http://localhost:4200")
public class PostController {

    private final PostService postService;
    private final FileServiceImpl fileService;

    @Value("${project.image}")
    private String path;

    public PostController(PostService postService, FileServiceImpl fileService) {
        this.postService = postService;
        this.fileService = fileService;
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

    // Upload Post Image
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Long postId
        ) throws IOException {

        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);
        postDto.setImageName(fileName);
        PostDto updatedPost = this.postService.updatePost(postDto, postId);

        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    @GetMapping(value = "/post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void postImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = null;
        resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
