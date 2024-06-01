package com.climbing.api.chat;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatRooms")
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
    private Long gymId; //짐 아이디

    @CreatedDate
    private LocalDateTime createdDate; // 생성 날짜

    public static ChatRoom of(String roomName, Long gymId) {
        return ChatRoom.builder().roomName(roomName).gymId(gymId).build();
    }
}
