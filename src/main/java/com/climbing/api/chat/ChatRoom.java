package com.climbing.api.chat;

import com.climbing.domain.gym.Gym;
import com.climbing.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity(name = "chat_room")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym; // 채팅이 이루어지는 짐

    @CreatedDate
    private LocalDateTime createdAt;

    public static ChatRoom of(Member member, Gym gym) {
        return ChatRoom.builder().member(member).gym(gym).build();
    }
}
