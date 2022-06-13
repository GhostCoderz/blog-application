package com.ghostcoderz.blog_application.controller;

import com.ghostcoderz.blog_application.payload.ApiResponse;
import com.ghostcoderz.blog_application.payload.CommentDto;
import com.ghostcoderz.blog_application.service.serviceInterface.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1")
@CrossOrigin("http://localhost:4200")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @RequestBody CommentDto commentDto,
            @PathVariable Long postId
    ){
        CommentDto createdComment = this.commentService.createComment(commentDto, postId);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable Long commentId
    ){
        this.commentService.deleteComment(commentId);
        ApiResponse apiResponse = new ApiResponse("Comment Deleted Successfully", true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
