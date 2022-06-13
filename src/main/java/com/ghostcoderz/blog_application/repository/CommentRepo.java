package com.ghostcoderz.blog_application.repository;

import com.ghostcoderz.blog_application.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Long> {

}
