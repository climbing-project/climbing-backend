package com.climbing.api.response;

import lombok.Getter;

public class PostGymResponse {
    @Getter
    private final Long id;

    private PostGymResponse(Long id) {
        this.id = id;
    }

    public static PostGymResponse from(Long id) {
        return new PostGymResponse(id);
    }
}
