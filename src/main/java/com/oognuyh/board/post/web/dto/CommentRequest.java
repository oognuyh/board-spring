package com.oognuyh.board.post.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.oognuyh.board.post.model.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private Long id;
    
    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @NotNull
    private Long postId;

    @NotNull
    private Long userId;

    public Comment toEntity() {
        return Comment.builder()
            .content(content)
            .build();
    }
}