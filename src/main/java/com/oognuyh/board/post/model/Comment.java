package com.oognuyh.board.post.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.oognuyh.board.global.model.BaseEntity;
import com.oognuyh.board.post.web.dto.CommentResponse;
import com.oognuyh.board.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public void update(String content) {
        this.content = content;
    }

    public CommentResponse toResponse() {
        return CommentResponse.builder()
            .id(id)
            .content(content)
            .authorId(user.getId())
            .author(user.getName())
            .postId(post.getId())
            .createdAt(createdAt)
            .modifiedAt(modifiedAt)
            .build();
    }
}