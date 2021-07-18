package com.oognuyh.board.post.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.oognuyh.board.post.model.Post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    
    private Long id;

    @NotEmpty
    @Size(max = 500)
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private String author;

    public Post toEntity() {
        return Post.builder()
            .id(id)
            .title(title)
            .content(content)
            .author(author)
            .build();
    }
}