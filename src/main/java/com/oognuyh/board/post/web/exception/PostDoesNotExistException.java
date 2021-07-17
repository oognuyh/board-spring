package com.oognuyh.board.post.web.exception;

public class PostDoesNotExistException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public PostDoesNotExistException(String message) {
        super(message);
    }
}