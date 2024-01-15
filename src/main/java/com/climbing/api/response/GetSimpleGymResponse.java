package com.climbing.api.response;

import com.climbing.domain.gym.Gym;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class GetSimpleGymResponse {
    @Getter
    private Long id;

    @Getter
    private String name;

    @Getter
    private String address;

    public GetSimpleGymResponse(Long id, String name, String address) {
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
