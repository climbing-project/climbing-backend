package com.climbing.api.chat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CHATROOMS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomName; // 사용자 닉네임

    @Column(nullable = false)
    private String createDate; // 생성 날짜

    @Column(nullable = false)
    private Long gymId; //짐 아이디

    public static ChatRoom of(String roomName, Long gymId, String createDate) {
        return ChatRoom.builder().roomName(roomName).gymId(gymId).createDate(createDate).build();
    }

}
