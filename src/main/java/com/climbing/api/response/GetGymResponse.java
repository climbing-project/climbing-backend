package com.climbing.api.response;

import com.climbing.domain.gym.Gym;
import lombok.Getter;

public class GetGymResponse {
    @Getter
    private Long id;

    @Getter
    private String name;

    @Getter
    private String address;

    public GetGymResponse(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static GetGymResponse from(Gym gym) {
        return new GetGymResponse(gym.getId(),
                                  gym.getName(),
                                  gym.getAddress());
    }
}
