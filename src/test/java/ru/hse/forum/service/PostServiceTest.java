package ru.hse.forum.service;

import ru.hse.forum.dto.PostRequest;
import ru.hse.forum.dto.PostDTO;
import ru.hse.forum.mapper.PostMapper;
import ru.hse.forum.model.Post;
import ru.hse.forum.model.Section;
import ru.hse.forum.model.User;
import ru.hse.forum.repository.PostRepository;
import ru.hse.forum.repository.SectionRepository;
import ru.hse.forum.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.emptyList;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
    @Mock
    private PostMapper postMapper;

    @Captor
    private ArgumentCaptor<Post> postArgumentCaptor;

    private PostService postService;

    @BeforeEach
    public void setup() {
        postService = new PostService(postRepository, sectionRepository, userRepository, authService, postMapper);
    }

    @Test
    @DisplayName("Should Retrieve Post by Id")
    public void shouldFindPostById() {
        Post post = new Post(123L, "First Post", "Test",
                0, null, Instant.now(), null);
        PostDTO expectedPostResponse = new PostDTO(123L, "First Post", "Test",
                "Test User", "Test Section", 1L,0, 0, false, false);

        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));
        Mockito.when(postMapper.mapToDto(Mockito.any(Post.class))).thenReturn(expectedPostResponse);

        PostDTO actualPostResponse = postService.getPost(123L);

        Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualPostResponse.getPostTitle()).isEqualTo(expectedPostResponse.getPostTitle());
    }

    @Test
    @DisplayName("Should Save Posts")
    public void shouldSavePosts() {
        User currentUser = new User(123L,
                "test user",
                "secret password",
                "user@email.com",
                Instant.now(),
                true,
                null,
                Collections.emptySet());
        Section section = new Section(123L, "First Forum Section", "Forum Section Description", emptyList(), Instant.now(), currentUser, Collections.emptySet());
        Post post = new Post(123L, "First Post", "Test", 0, null, Instant.now(), null);
        PostRequest postRequest = new PostRequest(null, 123L, "First Post", "Test");

        Mockito.when(sectionRepository.findById(123L))
                .thenReturn(Optional.of(section));
        Mockito.when(authService.getCurrentUser())
                .thenReturn(currentUser);
        Mockito.when(postMapper.map(postRequest, section, currentUser))
                .thenReturn(post);

        postService.save(postRequest);
        Mockito.verify(postRepository, Mockito.times(1)).save(postArgumentCaptor.capture());

        Assertions.assertThat(postArgumentCaptor.getValue().getPostId()).isEqualTo(123L);
        Assertions.assertThat(postArgumentCaptor.getValue().getPostTitle()).isEqualTo("First Post");
    }
}
