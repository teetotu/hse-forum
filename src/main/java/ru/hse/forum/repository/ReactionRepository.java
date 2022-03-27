package ru.hse.forum.repository;

import ru.hse.forum.model.Post;
import ru.hse.forum.model.User;
import ru.hse.forum.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findTopByPostAndUserOrderByReactionIdDesc(Post post, User currentUser);
}
