package ru.hse.forum.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.hse.forum.model.Section;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends PagingAndSortingRepository<Section, Long> {
    Optional<Section> findByName(String sectionName);

    @Query(value = "select * from forum_section where match(name) against(:searchString in natural language mode)", nativeQuery = true)
    List<Section> search(@Param("searchString") String searchString, Pageable pageable);
}
