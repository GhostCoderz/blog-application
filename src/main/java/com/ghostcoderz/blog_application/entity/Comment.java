package com.ghostcoderz.blog_application.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Setter
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

}
