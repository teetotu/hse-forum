package ru.hse.forum.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.forum.dto.PasswordChangeRequest;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.model.User;
import ru.hse.forum.repository.UserRepository;

import java.io.IOException;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

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
