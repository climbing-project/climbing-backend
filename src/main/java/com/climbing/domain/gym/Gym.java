package com.climbing.domain.gym;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Collections;
import java.util.List;

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

    @Getter
    private String description;

    @Getter
    private String tags;

    @Getter
    private String pricings;

    @Getter
    private String openHours;

    @Getter
    private String accommodations;

    @Getter
    private String contacts;

    @Getter
    private String grades;

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
