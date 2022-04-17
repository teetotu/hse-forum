package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.forum.dto.ReactionDto;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.exceptions.PostNotFoundException;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.ReactionService;

@RestController
@RequestMapping("/api/reaction")
@AllArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody ReactionDto reactionDto) {
        try {
            reactionService.react(reactionDto);
        } catch (HseForumException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (PostNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}