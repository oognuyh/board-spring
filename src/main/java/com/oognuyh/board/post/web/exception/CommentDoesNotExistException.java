package com.oognuyh.board.post.web.exception;

public class CommentDoesNotExistException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public CommentDoesNotExistException(String message) {
        super(message);
    }
}