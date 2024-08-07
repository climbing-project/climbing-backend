package com.climbing.domain.member;

import com.climbing.api.chat.ChatRoom;
import com.climbing.auth.oauth2.SocialType;
import com.climbing.domain.gym.Comment;
import com.climbing.domain.gym.Gym;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Table(name = "member")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    private String socialId;

    private boolean isBlocked = false;

    @OneToMany(mappedBy = "member")
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "member")
    private List<Gym> gyms;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void authorizeManager() {
        this.role = Role.MANAGER;
    }

    public void authorizeAdmin() {
        this.role = Role.ADMIN;
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    // 비밀번호 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword) {
        return passwordEncoder.matches(checkPassword, getPassword());
    }
}
