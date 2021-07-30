package com.oognuyh.board.post.web;

import java.util.List;

import javax.validation.Valid;

import com.oognuyh.board.post.service.CommentService;
import com.oognuyh.board.post.web.dto.CommentRequest;
import com.oognuyh.board.post.web.dto.CommentResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentApiController {
    private final CommentService commentService;
    
    @GetMapping
    public ResponseEntity<List<CommentResponse>> findCommentsByPostId(@RequestParam Long postId) {
        return new ResponseEntity<>(commentService.findCommentsByPostId(postId), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<CommentResponse> save(@Valid @RequestBody CommentRequest commentRequest) {
        return new ResponseEntity<>(commentService.save(commentRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<CommentResponse> update(@Valid @RequestBody CommentRequest commentRequest) {
        return new ResponseEntity<>(commentService.update(commentRequest), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
        commentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}