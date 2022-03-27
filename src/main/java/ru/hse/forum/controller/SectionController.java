package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.forum.dto.SectionDto;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/section")
@AllArgsConstructor
@Slf4j
public class SectionController {

    private final AuthService authService;
    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<SectionDto> createSection(@RequestBody SectionDto sectionDto) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sectionService.save(sectionDto));
    }

    @GetMapping("?page={page}")
    public ResponseEntity<List<SectionDto>> getAllSections(@PathVariable("page") Integer page) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sectionService.getPage(page, 30));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionDto> getSection(@PathVariable Long id) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sectionService.getSection(id));
    }

    @GetMapping("/search?keywords={keywords}&page={page}")
    public ResponseEntity<List<SectionDto>> getSectionByKeywordsInTitle(
            @PathVariable("keywords") String keywords, @PathVariable("page") Integer page) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sectionService.search(keywords, page, 30));
    }

    @PostMapping("/subscribe?{id}")
    public ResponseEntity<Void> subscribeToSection(@PathVariable("id") Long id) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        sectionService.subscribeUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}