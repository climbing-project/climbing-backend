package com.climbing.domain.gym;

import com.climbing.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "comment")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public final class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;
    @CreatedDate
    private LocalDate createdAt;
    private String text;

    public Comment(Gym gym, Member member, String text) {
        this.member = member;
        this.gym = gym;
        this.text = text;
    }
}
