package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.forum.dto.PostRequest;
import ru.hse.forum.dto.PostDTO;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.PostService;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest request) {
        postService.save(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<PostDTO>> getAllPosts(@RequestParam("page") Integer page) {
        
        return status(HttpStatus.OK).body(postService.getAllPosts(page, 30));
    }

    @GetMapping()
    public ResponseEntity<PostDTO> getPost(@RequestParam("id") Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @PutMapping
    public ResponseEntity<PostDTO> updatePost(@ModelAttribute PostRequest request) {
        try {
            PostDTO response = postService.updatePost(request);
            return status(HttpStatus.OK).body(response);
        } catch (HseForumException e) {
            return status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping()
    public ResponseEntity<Void> deletePost(@RequestParam("id") Long id) {
        try {
            postService.deletePost(id);
        } catch (HseForumException e) {
            return status(HttpStatus.UNAUTHORIZED).build();
        }
        return status(HttpStatus.OK).build();
    }

    @GetMapping("/bySection")
    public ResponseEntity<List<PostDTO>> getPostsBySection(@RequestParam("id") Long sectionId, @RequestParam("page") Integer page) {
        return status(HttpStatus.OK).body(postService.getPostsBySection(sectionId, page, 30));
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@RequestParam("username") String username, @RequestParam("page") Integer page) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username, page, 30));
    }

    @GetMapping("/byTitle")
    public ResponseEntity<List<PostDTO>> getPostsByKeywordsInTitle(@RequestParam("keywords") String keywords, @RequestParam("page") Integer page) {
        return status(HttpStatus.OK).body(postService.getPostsByKeywordsInTitle(keywords, page, 30));
    }

    @GetMapping("/bySubscriptions")
    public ResponseEntity<List<PostDTO>> getSubscriptionPosts(@RequestParam("page") Integer page) {
        return status(HttpStatus.OK).body(postService.getSubscriptionsPosts(page, 30));
    }

}
