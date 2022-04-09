package ru.hse.forum.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.forum.dto.*;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.model.User;
import ru.hse.forum.repository.UserRepository;

import javax.validation.Valid;
import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest request) {
        try {
            authService.signup(request);
        } catch (HseForumException | ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return new ResponseEntity<>("User Registration Successful", OK);
    }

    @PostMapping("verification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.deleteRefreshToken(request.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully");
    }


    public void setProfilePicture(MultipartFile picture) throws IOException {
        User user = authService.getCurrentUser();
        user.setProfilePicture(picture.getBytes());
        userRepository.save(user);
    }

    public byte[] getProfilePicture(Long id) {
        User user = userRepository.getById(id);
        byte[] file = user.getProfilePicture();
        if (file != null) {
            return file;
        } else {
            throw new HseForumException("No pp");
        }
    }

    public void updatePassword(PasswordChangeRequest request) {
        User user = authService.getCurrentUser();
        if (user.getPassword().equals(passwordEncoder.encode(request.getOldPassword()))) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new HseForumException("Wrong current password");
        }
    }
}
