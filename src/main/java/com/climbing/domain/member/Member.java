package com.climbing.domain.member;

import com.climbing.domain.gym.Gym;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Entity
public class Member {
    @Id
    private Long id;
//
//    @OneToMany(mappedBy = "member")
//    private List<Gym> gym;

    public static Member of() {
        return new Member();
    }
}
