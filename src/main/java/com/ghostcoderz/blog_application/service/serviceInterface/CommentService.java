package com.ghostcoderz.blog_application.service.serviceInterface;

import com.ghostcoderz.blog_application.payload.CommentDto;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto, Long postId);
    void deleteComment(Long commentId);

}
