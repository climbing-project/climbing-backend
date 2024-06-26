package com.climbing.api.chat.response;

import com.climbing.api.chat.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponse {

    private Long id;
    private Long memberId;
    private Long gymId;

    public static ChatRoomResponse of(ChatRoom room) {
        return new ChatRoomResponse(room.getId(), room.getMember().getId(), room.getGym().getId());
    }
}
