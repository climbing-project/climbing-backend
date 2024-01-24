package com.climbing.api.response;

import com.climbing.domain.gym.Gym;
import lombok.Getter;

public record GetGymResponse(Long id, String name, String address) {

    public static GetGymResponse from(Gym gym) {
        return new GetGymResponse(gym.getId(),
                                  gym.getName(),
                                  gym.getAddress());
    }
}
