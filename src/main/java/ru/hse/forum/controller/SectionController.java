package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hse.forum.dto.SectionDto;
import ru.hse.forum.exceptions.HseForumException;
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
    public ResponseEntity<SectionDto> createSection(@RequestBody SectionDto sectionDto, @RequestHeader("Authorization") String token) {
        log.info("------DEBUG " + token);
        try {
            SectionDto response = sectionService.save(sectionDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        } catch (HseForumException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SectionDto>> getAllSections(@RequestParam("page") Integer page) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sectionService.getPage(page, 30));
    }

    @GetMapping()
    public ResponseEntity<SectionDto> getSection(@RequestParam("id") Long id) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sectionService.getSection(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<SectionDto>> getSectionByKeywordsInTitle(
            @RequestParam("keywords") String keywords, @RequestParam("page") Integer page) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(sectionService.search(keywords, page, 30));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Void> subscribeToSection(@RequestParam("id") Long id) {
        sectionService.subscribeUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
