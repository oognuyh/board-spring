package com.oognuyh.board.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @DisplayName("게시글 생성")
    @Test
    void save() {
        // given
        Post post = Post.builder()
            .title("title")
            .content("content")
            .author("author")
            .build();

        // when
        Post savedPost = postRepository.save(post);

        // then
        assertAll(
            () -> assertThat(savedPost.getTitle()).isEqualTo(post.getTitle()),
            () -> assertThat(savedPost.getContent()).isEqualTo(post.getContent()),
            () -> assertThat(savedPost.getAuthor()).isEqualTo(post.getAuthor())
        );
    }

    @DisplayName("모든 게시글 조회")
    @Test
    void findAll() {
        // given
        var fakes = Arrays.asList(
            new Post(null, "title 1", "content", "author"),
            new Post(null, "title 2", "content", "author"),
            new Post(null, "title 3", "content", "author")
        );

        postRepository.saveAll(fakes);
        
        // when
        List<Post> posts = postRepository.findAll();

        // then
        assertThat(posts.size()).isEqualTo(fakes.size());
    }

    @DisplayName("게시글 수정")
    @Test
    void update() {
        // given
        Long id = postRepository.save(Post.builder()
            .title("title")
            .content("content")
            .author("author")
            .build()).getId();

        String newTitle = "new title";
        String newContent = "new content";

        // when
        Post post = postRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        
        post.update(newTitle, newContent);
        
        // then
        Post updatedPost = postRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        assertAll(
            () -> assertThat(updatedPost.getTitle()).isEqualTo(newTitle),
            () -> assertThat(updatedPost.getContent()).isEqualTo(newContent)
        );
    }

    @DisplayName("게시글 삭제")
    @Test    
    void delete() {
        // given
        Long id = postRepository.save(Post.builder()
            .title("title")
            .content("content")
            .author("author")
            .build()).getId();

        // when
        postRepository.deleteById(id);

        // then
        assertTrue(postRepository.findById(id).isEmpty());
    }
}