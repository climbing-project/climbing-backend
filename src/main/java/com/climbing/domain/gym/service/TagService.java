package com.climbing.domain.gym.service;

import com.climbing.api.command.PostTagCommand;
import com.climbing.domain.gym.Tag;
import com.climbing.domain.gym.repository.TagRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    public void addTag(PostTagCommand command) {
        String tag = command.tag();
        if (tagRepository.existsByValue(tag)) {
            return;
        }
        tagRepository.save(new Tag(tag));
    }
}
