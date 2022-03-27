package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.forum.dto.ReactionDto;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.ReactionService;

@RestController
@RequestMapping("/api/reactions/")
@AllArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody ReactionDto reactionDto) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        reactionService.react(reactionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}