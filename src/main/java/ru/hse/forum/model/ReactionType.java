package ru.hse.forum.model;

import ru.hse.forum.exceptions.HseForumException;

import java.util.Arrays;

public enum ReactionType {
    UPVOTE(1), DOWNVOTE(-1),
    ;

    private int type;

    ReactionType(int direction) {
    }

    public static ReactionType lookup(Integer reactionType) {
        return Arrays.stream(ReactionType.values())
                .filter(value -> value.getType().equals(reactionType))
                .findAny()
                .orElseThrow(() -> new HseForumException("Reaction not found"));
    }

    public Integer getType() {
        return type;
    }
}
