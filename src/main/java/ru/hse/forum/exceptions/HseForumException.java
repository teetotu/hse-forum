package ru.hse.forum.exceptions;

public class HseForumException extends RuntimeException {
    public HseForumException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public HseForumException(String exMessage) {
        super(exMessage);
    }
}
