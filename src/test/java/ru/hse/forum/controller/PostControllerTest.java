package ru.hse.forum.controller;

import ru.hse.forum.dto.PostDTO;
import ru.hse.forum.security.JwtProvider;
import ru.hse.forum.service.PostService;
import ru.hse.forum.service.UserDetailsServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @MockBean
    private PostService postService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should List All Posts When making GET request to endpoint - /api/posts/")
    void shouldCreatePost() throws Exception {
        PostDTO postRequest1 = new PostDTO(1L, "Post Name", "Description", "User 1",
                "Forum Section 1", 0, 0, false, false);
        PostDTO postRequest2 = new PostDTO(2L, "Post Name 2", "Description2", "User 2",
                "Forum Section 2", 0, 0, false, false);

        Mockito.when(postService.getAllPosts(1, 30)).thenReturn(asList(postRequest1, postRequest2));

        mockMvc.perform(get("/api/posts/"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].postTitle", Matchers.is("Post Name")))
                .andExpect(jsonPath("$[1].postTitle", Matchers.is("Post Name 2")))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
    }
}
