package ru.hse.forum.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.hse.forum.dto.PostRequest;
import ru.hse.forum.dto.PostDTO;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.exceptions.PostNotFoundException;
import ru.hse.forum.exceptions.SectionNotFoundException;
import ru.hse.forum.mapper.PostMapper;
import ru.hse.forum.model.Post;
import ru.hse.forum.model.Section;
import ru.hse.forum.model.User;
import ru.hse.forum.repository.PostRepository;
import ru.hse.forum.repository.SectionRepository;
import ru.hse.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SectionRepository sectionRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Section section =
                sectionRepository
                        .findById(postRequest.getSectionId())
                        .orElseThrow(() -> new SectionNotFoundException(postRequest.getSectionId().toString()));
        postRepository.save(postMapper.map(postRequest, section, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostDTO getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts(int page, int size) {
        return postRepository.findAll(PageRequest.of(page, size, Sort.by("date").ascending()))
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPostsBySection(Long sectionId, int page, int size) {
        Section section =
                sectionRepository
                        .findById(sectionId)
                        .orElseThrow(() -> new SectionNotFoundException(sectionId.toString()));
        List<Post> posts = postRepository.findAllBySection(section, PageRequest.of(page, size, Sort.by("date").ascending()));
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPostsByUsername(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findAllByUser(user, PageRequest.of(page, size, Sort.by("date").ascending()))
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostDTO> getPostsByKeywordsInTitle(String userInput, int page, int size) {
        return postRepository
                .search(userInput, PageRequest.of(page, size, Sort.by("date").ascending()))
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    public PostDTO updatePost(PostRequest postRequest) {
        Post post = postRepository.findById(postRequest.getPostId()).orElseThrow(() -> new HseForumException("Could find post with id: " + postRequest.getPostId()));
        if (post.getUser() != authService.getCurrentUser())
            throw new HseForumException("Post [id:" + postRequest.getPostId() + "] doesn't belong to " +
                    "user [username:" + authService.getCurrentUser().getUsername() + "]");

        if (postRequest.getPostTitle() != null) {
            post.setPostTitle(postRequest.getPostTitle());
        }
        if (postRequest.getContent() != null) {
            post.setContent(postRequest.getContent());
        }
        postRepository.save(post);
        return postMapper.mapToDto(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new HseForumException("Could find post with id: " + id));
        if (post.getUser() != authService.getCurrentUser())
            throw new HseForumException("Post [id:" + id + "] doesn't belong to " +
                    "user [username:" + authService.getCurrentUser().getUsername() + "]");
        postRepository.deleteById(id);
    }

    public List<PostDTO> getSubscriptionsPosts(int page, int size) {
        return postRepository
                .findAllBySectionIn(authService.getCurrentUser().getSubscriptions(), PageRequest.of(page, size))
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
