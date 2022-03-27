package ru.hse.forum.dto;

import ru.hse.forum.model.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionDto {
    private ReactionType reactionType;
    private Long postId;
}
