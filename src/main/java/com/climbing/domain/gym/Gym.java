package com.climbing.domain.gym;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Gym {
    @Id
    @Getter
    private Long id;

    @Getter
    private String name;

    @Getter
    private String address;

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    @Getter
//    private Member member;

    public static Gym of(Long id, String name, String address) {
        return Gym.builder()
                .id(id)
                .name(name)
                .address(address)
                .build();
    }
}
