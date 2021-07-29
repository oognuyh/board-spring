package com.oognuyh.board.post.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.oognuyh.board.post.model.Comment;
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
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Post savedPost;

    private User savedUser;

    private Comment savedComment;

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

        savedUser = userRepository.save(user);

        Post post = Post.builder()
            .title("title")
            .content("content")
            .user(savedUser)
            .build();

        savedPost = postRepository.save(post);

        Comment comment = Comment.builder()
            .content("content")
            .post(savedPost)
            .user(savedUser)
            .build();

        // when
        savedComment = commentRepository.save(comment);

        // then
        assertAll(
            () -> assertThat(savedComment.getPost().getId()).isEqualTo(comment.getPost().getId()),
            () -> assertThat(savedComment.getContent()).isEqualTo(comment.getContent())
        );
    }

    @DisplayName("게시글 아이디를 통한 댓글 조회")
    @Test
    void find() {
        // when
        List<Comment> comments = commentRepository.findCommentsByPostIdOrderByCreatedAtAsc(savedPost.getId());

        // then
        assertThat(comments.stream().allMatch(comment -> comment.getPost().getId() == savedPost.getId())).isTrue();
    }

    @DisplayName("댓글 수정")
    @Test
    void update() {
        // given
        Comment comment = commentRepository.findById(savedComment.getId())
            .orElseThrow(IllegalArgumentException::new);
        String newContent = "new content";

        // when
        comment.update(newContent);

        // then
        Comment updatedComment = commentRepository.findById(comment.getId())
            .orElseThrow(IllegalArgumentException::new);

        assertThat(comment.getContent()).isEqualTo(updatedComment.getContent());
    }

    @DisplayName("댓글 삭제")
    @Test
    void delete() {
        // when
        commentRepository.deleteById(savedComment.getId());

        // then
        assertTrue(commentRepository.findById(savedComment.getId()).isEmpty());
    }
}