package ru.hse.forum.mapper;

import ru.hse.forum.dto.SectionDto;
import ru.hse.forum.model.Post;
import ru.hse.forum.model.Section;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectionMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(section.getPosts()))")
    SectionDto mapSectionToDto(Section section);

    default Integer mapPosts(List<Post> posts) {
        return posts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Section mapDtoToSection(SectionDto sectionDto);
}
