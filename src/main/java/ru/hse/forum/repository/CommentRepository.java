package ru.hse.forum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.hse.forum.model.Comment;
import ru.hse.forum.model.Post;
import ru.hse.forum.model.User;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    List<Comment> findByPost(Post post, Pageable pageable);

    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user, Pageable pageable);

    List<Comment> findAllByUser(User user);
}
