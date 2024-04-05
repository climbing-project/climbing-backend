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

    public GetSimpleGymResponse(Long id, String name, String jibunAddress, String roadAddress, String unitAddress) {
        this.id = id;
        this.name = name;
        this.address = new Address(jibunAddress, roadAddress, unitAddress);
    }

    public static List<GetSimpleGymResponse> from(List<Gym> gyms) {
        return gyms.stream()
                .map(gym -> new GetSimpleGymResponse(gym.getId(),
                                                     gym.getName(),
                                                     gym.getJibunAddress(),
                                                     gym.getRoadAddress(),
                                                     gym.getUnitAddress()))
                .collect(Collectors.toList());
    }
}
