package ru.hse.forum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.forum.model.User;
import ru.hse.forum.repository.UserRepository;

import java.io.IOException;

@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public void setProfilePicture(MultipartFile picture) throws IOException {
        User user = authService.getCurrentUser();
        user.setProfilePicture(picture.getBytes());
        userRepository.save(user);
    }

    public byte[] getProfilePicture(Long id) {
        User user = userRepository.getById(id);
        return user.getProfilePicture();
    }
}
