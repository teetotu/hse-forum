package ru.hse.forum.service;

import org.springframework.transaction.annotation.Transactional;
import ru.hse.forum.dto.CommentDto;
import ru.hse.forum.exceptions.PostNotFoundException;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.mapper.CommentMapper;
import ru.hse.forum.model.Comment;
import ru.hse.forum.model.NotificationEmail;
import ru.hse.forum.model.Post;
import ru.hse.forum.model.User;
import ru.hse.forum.repository.CommentRepository;
import ru.hse.forum.repository.PostRepository;
import ru.hse.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public void save(CommentDto commentDto) {
        Post post = postRepository
                .findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
    }

    public List<CommentDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }
}
