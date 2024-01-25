package com.climbing.api.response;

import com.climbing.domain.gym.Gym;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class GetSimpleGymResponse {
    @Getter
    private final Long id;
    @Getter
    private final String name;
    @Getter
    private final String address;

    private GetSimpleGymResponse(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static List<GetSimpleGymResponse> from(List<Gym> gyms) {
        return gyms.stream()
                .map(gym -> new GetSimpleGymResponse(gym.getId(),
                                                     gym.getName(),
                                                     gym.getAddress()))
                .collect(Collectors.toList());
    }
}
