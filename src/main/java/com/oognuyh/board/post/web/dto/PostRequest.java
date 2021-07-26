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

    @NotEmpty(message = "제목을 입력하세요.")
    @Size(max = 100, message = "100자 이하로 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요")
    private String content;

    @NotNull(message = "작성자 없이 작성할 수 없습니다")
    private Long userId;

    public Post toEntity() {
        return Post.builder()
            .id(id)
            .title(title)
            .content(content)
            .build();
    }
}