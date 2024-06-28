package com.climbing.api.response;

import com.climbing.domain.gym.Address;
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
    private final Address address;

    public GetSimpleGymResponse(Long id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public static List<GetSimpleGymResponse> from(List<Gym> gyms) {
        return gyms.stream()
                .map(gym -> new GetSimpleGymResponse(gym.getId(),
                                                     gym.getName(),
                                                     new Address(gym.getJibunAddress(),
                                                             gym.getRoadAddress(),
                                                             gym.getUnitAddress())))
                .collect(Collectors.toList());
    }
}
