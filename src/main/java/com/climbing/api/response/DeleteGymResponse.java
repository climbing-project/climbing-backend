package com.climbing.api.response;

import lombok.Getter;

public class DeleteGymResponse {
    @Getter
    private Long gymId;

    public DeleteGymResponse(Long gymId) {
        this.gymId = gymId;
    }

    public static DeleteGymResponse from(Long gymId) {
        return new DeleteGymResponse(gymId);
    }
}
