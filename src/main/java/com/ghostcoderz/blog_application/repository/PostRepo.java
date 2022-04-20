package com.ghostcoderz.blog_application.repository;

import com.ghostcoderz.blog_application.entity.Category;
import com.ghostcoderz.blog_application.entity.Post;
import com.ghostcoderz.blog_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo  extends JpaRepository<Post, Long> {

    List<Post> findByUser(User user);
    List<Post> findByCategory(Category category);

}
