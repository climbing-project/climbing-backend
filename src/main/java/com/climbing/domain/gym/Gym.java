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

    public static Gym of(String name, String address, String description, String tags, String pricings, String openHours, String accommodations, String contacts, String grade) {
        return Gym.builder()
                .id(null)
                .name(name)
                .address(address)
                .description(description)
                .tags(tags)
                .pricings(pricings)
                .openHours(openHours)
                .accommodations(accommodations)
                .contacts(contacts)
                .grades(grade)
                .build();
    }

    public void update(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
