package com.climbing.domain.gym;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Gym {
    @Id
    @GeneratedValue
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

    @Getter
    private int hits;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    @Getter
//    private Member member;

    public static Gym of(String name, String address, String description, String tags, String pricings, String openHours, String accommodations, String contacts, String grades) {
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
                .grades(grades)
                .build();
    }

    public void update(String name, String address, String description, String tags, String pricings, String openHours, String accommodations, String contacts, String grades) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.tags = tags;
        this.pricings = pricings;
        this.openHours = openHours;
        this.accommodations = accommodations;
        this.contacts = contacts;
        this.grades = grades;
    }
}
