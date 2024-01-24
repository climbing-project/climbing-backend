package com.climbing.api.response;

import lombok.Getter;

public record PostGymResponse(@Getter Long id) {

    public static PostGymResponse from(Long id) {
        return new PostGymResponse(id);
    }
}
