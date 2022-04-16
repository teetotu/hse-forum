package ru.hse.forum.controller;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.forum.dto.*;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.service.AuthService;
import ru.hse.forum.service.RefreshTokenService;
import ru.hse.forum.service.UserService;

import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.signup(registerRequest);
        } catch (HseForumException | ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return new ResponseEntity<>("User Registration Successful", OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authService.login(loginRequest), OK);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }


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

    @GetMapping("/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@RequestParam("id") Long id) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            byte[] pp = userService.getProfilePicture(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + System.currentTimeMillis() + "\"")
                    .body(pp);
        } catch (HseForumException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordChangeRequest request) {
        if (!authService.getCurrentUser().isEnabled()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            userService.updatePassword(request);
            return ResponseEntity.status(OK).body("Password changed successfully");
        }
        catch (HseForumException e) {
            return ResponseEntity.status(FORBIDDEN).body(e.getMessage());
        }
    }
}
