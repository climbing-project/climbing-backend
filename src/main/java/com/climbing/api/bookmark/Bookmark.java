package com.climbing.api.bookmark;

import com.climbing.domain.gym.Gym;
import com.climbing.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Gym gym;

    @Column(nullable = false)
    private boolean status;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Bookmark of(Member member, Gym gym) {
        return Bookmark.builder().member(member).gym(gym).status(true).build();
    }

    public void deleteBookmark() {
        this.status = false;
    }
}
