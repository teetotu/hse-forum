package ru.hse.forum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.forum.model.Section;
import ru.hse.forum.model.Post;
import ru.hse.forum.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    List<Post> findAllBySection(Section section, Pageable pageable);

    List<Post> findAllBySectionIn(Set<Section> subscriptions, Pageable pageable);

    List<Post> findAllByUser(User user, Pageable pageable);

    @Query(value = "SELECT p FROM Post p WHERE ftsp(:searchString) = true")
    List<Post> search(@Param("searchString") String searchString, Pageable pageable);
}
