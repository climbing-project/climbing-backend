package com.climbing.api.controller;

import com.climbing.api.command.PostTagCommand;
import com.climbing.api.request.PostTagRequest;
import com.climbing.api.response.GetTagsResponse;
import com.climbing.domain.gym.Tag;
import com.climbing.domain.gym.service.TagService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<GetTagsResponse> getTags() {
        List<Tag> tags = tagService.getTags();
        return new ResponseEntity<>(GetTagsResponse.from(tags), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addTag(@RequestBody PostTagRequest request) {
        PostTagCommand command = request.toCommand();
        tagService.addTag(command);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
