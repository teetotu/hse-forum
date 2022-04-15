package ru.hse.forum.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.hse.forum.exceptions.HseForumException;
import ru.hse.forum.exceptions.SectionNotFoundException;
import ru.hse.forum.model.Section;
import ru.hse.forum.model.User;
import ru.hse.forum.repository.SectionRepository;
import ru.hse.forum.dto.SectionDto;
import ru.hse.forum.mapper.SectionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.forum.repository.UserRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SectionService {

    private final SectionRepository sectionRepository;
    private final SectionMapper sectionMapper;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Transactional
    public SectionDto save(SectionDto sectionDto) {
        Section save = sectionRepository.save(sectionMapper.mapDtoToSection(sectionDto, authService.getCurrentUser()));
        sectionDto.setId(save.getId());
        return sectionDto;
    }

    @Transactional(readOnly = true)
    public List<SectionDto> getPage(int page, int size) {
        return sectionRepository
                .findAll(PageRequest.of(page, size, Sort.by("date").ascending()))
                .stream()
                .map(sectionMapper::mapSectionToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public SectionDto getSection(Long id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new SectionNotFoundException("No forum section found with ID - " + id));
        return sectionMapper.mapSectionToDto(section);
    }

    @Transactional(readOnly = true)
    public List<SectionDto> search(String searchString, int page, int size) {
        return sectionRepository
                .search(searchString, PageRequest.of(page, size, Sort.by("date").ascending()))
                .stream()
                .map(sectionMapper::mapSectionToDto)
                .collect(toList());
    }

    public void subscribeUser(Long id) {
        Section section = sectionRepository.findById(id).orElseThrow(() -> new SectionNotFoundException("No forum section found with ID - " + id));
        User user = authService.getCurrentUser();
        section.getSubscribers().add(user);
        user.getSubscriptions().add(section);
        sectionRepository.save(section);
        userRepository.save(user);
    }
}
