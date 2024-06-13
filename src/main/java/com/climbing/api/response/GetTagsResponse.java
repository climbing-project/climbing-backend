package com.climbing.api.response;

import com.climbing.domain.gym.Tag;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTagsResponse {
    private List<String> tags;

    private GetTagsResponse(List<Tag> tags) {
        this.tags = tags.stream().map(Tag::getValue).toList();
    }

    public static GetTagsResponse from(List<Tag> tags) {
        return new GetTagsResponse(tags);
    }
}
