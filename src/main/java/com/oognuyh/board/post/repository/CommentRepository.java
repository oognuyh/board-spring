package com.oognuyh.board.post.repository;

import java.util.List;

import com.oognuyh.board.post.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findCommentsByPostIdOrderByCreatedAtAsc(Long postId);
    Integer countByPostId(Long postId);
}