package com.climbing.api.response;

public record PostGymResponse(Long id) {

    public static PostGymResponse from(Long id) {
        return new PostGymResponse(id);
    }
}
