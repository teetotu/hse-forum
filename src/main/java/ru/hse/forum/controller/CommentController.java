package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.forum.dto.CommentDto;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.CommentService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comment/")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {
        commentService.save(commentDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/byPost?post_id={postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsForPost(postId));
    }
}
