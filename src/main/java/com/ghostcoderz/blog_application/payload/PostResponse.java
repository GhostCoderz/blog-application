package com.ghostcoderz.blog_application.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponse {

    private List<PostDto> content;
    private long pageNumber;
    private long pageSize;
    private long totalPages;
    private long totalElements;
    private boolean isLastPage;

}
