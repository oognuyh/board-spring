package com.oognuyh.board.post.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.oognuyh.board.post.model.Post;
import com.oognuyh.board.user.model.Roles;
import com.oognuyh.board.user.model.User;
import com.oognuyh.board.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private UserRepository userRepository;

    private Post savedPost;

    @BeforeEach
    void save() {
        // given
        User user = User.builder()
            .name("name")
            .password("password")
            .email("email@email.com")
            .phoneNumber("01000000000")
            .role(Roles.ROLE_USER)
            .build();

        Post post = Post.builder()
            .title("title")
            .content("content")
            .user(userRepository.save(user))
            .build();
        
        // when
        savedPost = postRepository.save(post);

        // then
        assertAll(
            () -> assertThat(savedPost.getTitle()).isEqualTo(post.getTitle()),
            () -> assertThat(savedPost.getContent()).isEqualTo(post.getContent())
        );
    }

    @DisplayName("게시글 조회")
    @Test
    void find() {        
        // when
        List<Post> posts = postRepository.findAll();

        // then
        assertThat(posts.size()).isEqualTo(1);
    }

    @DisplayName("게시글 수정")
    @Test
    void update() {
        // given
        String newTitle = "new title";
        String newContent = "new content";

        // when
        Post post = postRepository.findById(savedPost.getId())
            .orElseThrow(IllegalArgumentException::new);
        
        post.update(newTitle, newContent);
        
        // then
        Post updatedPost = postRepository.findById(post.getId())
            .orElseThrow(IllegalArgumentException::new);

        assertAll(
            () -> assertThat(updatedPost.getTitle()).isEqualTo(newTitle),
            () -> assertThat(updatedPost.getContent()).isEqualTo(newContent)
        );
    }

    @DisplayName("게시글 삭제")
    @Test    
    void delete() {
        // when
        postRepository.deleteById(savedPost.getId());

        // then
        assertTrue(postRepository.findById(savedPost.getId()).isEmpty());
    }
}