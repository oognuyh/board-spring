package com.oognuyh.board.post.web;

import javax.validation.Valid;

import com.oognuyh.board.post.service.PostService;
import com.oognuyh.board.post.web.dto.PostRequest;
import com.oognuyh.board.post.web.dto.PostResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostApiController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getPosts(@RequestParam(name = "page", defaultValue = "1") Integer page, 
                                                       @RequestParam(name = "size", defaultValue = "2") Integer size) {
        System.out.println("page: " + page.toString() + " size: " + size.toString());
        return new ResponseEntity<>(postService.findPosts(PageRequest.of(page, size, Sort.by("createdAt").descending())), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        return new ResponseEntity<>(postService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PostResponse> save(@Valid @RequestBody PostRequest postRequest) {
        return new ResponseEntity<>(postService.save(postRequest), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<PostResponse> update(@Valid @RequestBody PostRequest postRequest) {
        return new ResponseEntity<>(postService.update(postRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        postService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}