package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.forum.dto.PostRequest;
import ru.hse.forum.dto.PostResponse;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.PostService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("?page={page}")
    public ResponseEntity<List<PostResponse>> getAllPosts(@PathVariable("page") Integer page) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return status(HttpStatus.OK).body(postService.getAllPosts(page, 30));
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @PutMapping
    public ResponseEntity<PostResponse> updatePost(@ModelAttribute PostRequest postRequest) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            PostResponse response = postService.updatePost(postRequest);
            return status(HttpStatus.OK).body(response);
        } catch (HseForumException e) {
            return status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            postService.deletePost(id);
        } catch (HseForumException e) {
            return status(HttpStatus.UNAUTHORIZED).build();
        }
        return status(HttpStatus.OK).build();
    }

    @GetMapping("by-section/{id}?page={page}")
    public ResponseEntity<List<PostResponse>> getPostsBySection(@PathVariable("id") Long id, @PathVariable("page") Integer page) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return status(HttpStatus.OK).body(postService.getPostsBySection(id, page, 30));
    }

    @GetMapping("by-user/{username}?page={page}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable("username") String username, @PathVariable("page") Integer page) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username, page, 30));
    }

    @GetMapping("by-title/{keywords}?page={page}")
    public ResponseEntity<List<PostResponse>> getPostsByKeywordsInTitle(@PathVariable("keywords") String keywords, @PathVariable("page") Integer page) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return status(HttpStatus.OK).body(postService.getPostsByKeywordsInTitle(keywords, page, 30));
    }

    @GetMapping("by-subsriptions?page={page}")
    public ResponseEntity<List<PostResponse>> getSubscriptionPosts(@PathVariable("page") Integer page) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return status(HttpStatus.OK).body(postService.getSubscriptionsPosts(page, 30));
    }

}
