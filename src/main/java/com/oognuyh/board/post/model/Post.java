package com.oognuyh.board.post.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.oognuyh.board.global.model.BaseEntity;
import com.oognuyh.board.post.web.dto.PostResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostResponse toResponse() {
        return PostResponse.builder()
            .id(id)
            .title(title)
            .content(content)
            .author(author)
            .createdAt(createdAt)
            .modifiedAt(modifiedAt)
            .build();
    }
}