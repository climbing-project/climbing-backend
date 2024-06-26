package com.climbing.api.bookmark.response;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Gym;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyBookmarkResponse {
    private Long gymId;
    private String gymName;
    private Address address;

    public static MyBookmarkResponse of(Gym gym) {
        return new MyBookmarkResponse(gym.getId(), gym.getName(), new Address(gym.getJibunAddress(), gym.getRoadAddress(), gym.getUnitAddress()));
    }
}
