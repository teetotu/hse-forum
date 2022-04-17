package ru.hse.forum.mapper;

import ru.hse.forum.dto.PostRequest;
import ru.hse.forum.dto.PostDTO;
import ru.hse.forum.model.ReactionType;
import ru.hse.forum.repository.CommentRepository;
import ru.hse.forum.repository.ReactionRepository;
import ru.hse.forum.model.*;
import ru.hse.forum.service.AuthService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private AuthService authService;


    @Mapping(target = "date", expression = "java(java.time.Instant.now())")
    @Mapping(target = "content", source = "postRequest.content")
    @Mapping(target = "section", source = "section")
    @Mapping(target = "reactionCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Section section, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "sectionId", source = "section.id")
    @Mapping(target = "sectionName", source = "section.name")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "upvote", expression = "java(isPostUpvoted(post))")
    @Mapping(target = "downvote", expression = "java(isPostDownvoted(post))")
    public abstract PostDTO mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    boolean isPostUpvoted(Post post) {
        return checkReactionType(post, ReactionType.UPVOTE);
    }

    boolean isPostDownvoted(Post post) {
        return checkReactionType(post, ReactionType.DOWNVOTE);
    }

    private boolean checkReactionType(Post post, ReactionType reactionType) {
        if (authService.isLoggedIn()) {
            Optional<Reaction> reactionForPostByUser =
                    reactionRepository.findTopByPostAndUserOrderByReactionIdDesc(post, authService.getCurrentUser());
            return reactionForPostByUser
                    .filter(reaction -> reaction.getReactionType().equals(reactionType))
                    .isPresent();
        }
        return false;
    }

}