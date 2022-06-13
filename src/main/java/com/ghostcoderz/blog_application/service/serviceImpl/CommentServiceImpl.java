package com.ghostcoderz.blog_application.service.serviceImpl;

import com.ghostcoderz.blog_application.entity.Comment;
import com.ghostcoderz.blog_application.entity.Post;
import com.ghostcoderz.blog_application.exceptions.ResourceNotFoundException;
import com.ghostcoderz.blog_application.payload.CommentDto;
import com.ghostcoderz.blog_application.repository.CommentRepo;
import com.ghostcoderz.blog_application.repository.PostRepo;
import com.ghostcoderz.blog_application.service.serviceInterface.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepo postRepo;
    private final CommentRepo commentRepo;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(PostRepo postRepo,
                              CommentRepo commentRepo,
                              ModelMapper modelMapper) {
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Long postId) {

        Post post = this.postRepo.findById(postId)
                .orElseThrow( () ->
                    new ResourceNotFoundException("Post", "post id", postId)
                );
        Comment comment = dtoToComment(commentDto);
        comment.setPost(post);
        Comment savedComment = this.commentRepo.save(comment);
        return commentToDto(savedComment);
    }

    @Override
    public void deleteComment(Long commentId) {

        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment", "comment id", commentId)
                );
        this.commentRepo.delete(comment);
    }

    private CommentDto commentToDto(Comment comment){
        return this.modelMapper.map(comment, CommentDto.class);
    }

    private Comment dtoToComment(CommentDto commentDto){
        return this.modelMapper.map(commentDto, Comment.class);
    }

}
