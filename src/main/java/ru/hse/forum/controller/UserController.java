package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping(path = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam MultipartFile file) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            userService.setProfilePicture(file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/profile-picture/{id}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable("id") Long id) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        byte[] pp = userService.getProfilePicture(id);
        if (pp != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + System.currentTimeMillis() + "\"")
                    .body(pp);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
