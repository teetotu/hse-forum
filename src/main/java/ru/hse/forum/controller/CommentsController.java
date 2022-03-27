package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.forum.dto.CommentsDto;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.CommentService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        commentService.save(commentsDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/byPost/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String username){
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForUser(username));
    }

}
