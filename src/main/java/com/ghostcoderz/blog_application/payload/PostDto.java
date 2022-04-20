package com.ghostcoderz.blog_application.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private Long postId;
    private String title;
    private String content;
    private String imageName;
    private Date createDt;
    private CategoryDto category;
    private UserDto user;

}
