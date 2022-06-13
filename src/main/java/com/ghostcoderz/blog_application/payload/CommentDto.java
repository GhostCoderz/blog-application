package com.ghostcoderz.blog_application.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    private long commentId;
    private String content;

}
