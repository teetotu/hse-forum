package ru.hse.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String postTitle;
    private String description;
    private String username;
    private String sectionName;
    private Integer reactionCount;
    private Integer commentCount;
    private boolean upvote;
    private boolean downvote;
}
