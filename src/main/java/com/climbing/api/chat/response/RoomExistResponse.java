package com.climbing.api.chat.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomExistResponse {
    private boolean exists;
    private Long roomId;

    public static RoomExistResponse of(boolean exists, Long roomId) {
        return new RoomExistResponse(exists, roomId);
    }
}
