package com.ghostcoderz.blog_application.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Integer categoryId;

    @NotBlank
    @Size(min = 4,
            max = 100,
            message = "Title must be minimum of 4 characters " +
                    "and maximum of 100 characters")
    private String categoryTitle;

    @NotBlank
    @Size(min = 10,
            message = "Category Description must be minimum of 10 characters " +
                    "and maximum of "+ Integer.MAX_VALUE +" characters")
    private String categoryDescription;

}
