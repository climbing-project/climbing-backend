package com.climbing.api.response;

import com.climbing.domain.gym.Gym;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public record GetSimpleGymResponse(Long id, String name, String address) {
    public static List<GetSimpleGymResponse> from(List<Gym> gyms) {
        return gyms.stream()
                .map(gym -> new GetSimpleGymResponse(gym.getId(),
                                                     gym.getName(),
                                                     gym.getAddress()))
                .collect(Collectors.toList());

    }
}
