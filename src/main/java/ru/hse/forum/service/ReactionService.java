package ru.hse.forum.service;

import ru.hse.forum.exceptions.PostNotFoundException;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.model.Post;
import ru.hse.forum.model.Reaction;
import ru.hse.forum.model.ReactionType;
import ru.hse.forum.repository.PostRepository;
import ru.hse.forum.repository.ReactionRepository;
import ru.hse.forum.dto.ReactionDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void react(ReactionDto reactionDto) {
        Post post = postRepository
                .findById(reactionDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + reactionDto.getPostId()));
        Optional<Reaction> reactionByPostAndUser =
                reactionRepository.findTopByPostAndUserOrderByReactionIdDesc(post, authService.getCurrentUser());
        if (reactionByPostAndUser.isPresent() &&
                reactionByPostAndUser.get().getReactionType().equals(reactionDto.getReactionType())) {
            throw new HseForumException("You have already " + reactionDto.getReactionType() + "'d for this post");
        }
        if (ReactionType.UPVOTE.equals(reactionDto.getReactionType())) {
            post.setReactionCount(post.getReactionCount() + 1);
        } else {
            post.setReactionCount(post.getReactionCount() - 1);
        }
        reactionRepository.save(mapToReaction(reactionDto, post));
        postRepository.save(post);
    }

    private Reaction mapToReaction(ReactionDto reactionDto, Post post) {
        return Reaction.builder()
                .reactionType(reactionDto.getReactionType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
