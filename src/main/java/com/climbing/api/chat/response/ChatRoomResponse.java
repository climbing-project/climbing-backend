package com.climbing.api.chat.response;

import com.climbing.api.chat.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomResponse {

    private Long id;
    private String roomName;
    private Long gymId;

    public static ChatRoomResponse of(ChatRoom room) {
        return new ChatRoomResponse(room.getId(), room.getRoomName(), room.getGym().getId());
    }
}
