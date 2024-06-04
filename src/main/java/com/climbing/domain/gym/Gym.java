package com.climbing.domain.gym;

import com.climbing.api.chat.ChatRoom;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "gym")
@EntityListeners(AuditingEntityListener.class)
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private String jibunAddress;
    @Getter
    private String roadAddress;
    @Getter
    private String unitAddress;
    @Getter
    private float latitude;
    @Getter
    private float longitude;
    @Getter
    private String description;
    @Getter
    private LocalDate latestSettingDate;
    @Getter
    private String twitter;
    @Getter
    private String facebook;
    @Getter
    private String instagram;
    @Getter
    private String homepage;
    @Getter
    private String images;
    @Getter
    private String tags;
    @Getter
    private String pricing;
    @Getter
    private String openHours;
    @Getter
    private String accommodations;
    @Getter
    private String contact;
    @Getter
    private String grades;
    @Getter
    private String comments;
    @Getter
    private int likes;
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

    @Getter
    @OneToMany(mappedBy = "gym")
    private List<ChatRoom> chatRooms;

    public static Gym of(String name, String jibunAddress, String roadAddress, String unitAddress, float latitude, float longitude, String description, String tags, String pricing, String openHours, String accommodations, String contact, String grades) {
        return Gym.builder()
                .id(null)
                .name(name)
                .jibunAddress(jibunAddress)
                .roadAddress(roadAddress)
                .unitAddress(unitAddress)
                .latitude(latitude)
                .longitude(longitude)
                .description(description)
                .tags(tags)
                .pricing(pricing)
                .openHours(openHours)
                .accommodations(accommodations)
                .contact(contact)
                .grades(grades)
                .build();
    }

    public void update(String name, String jibunAddress, String roadAddress, String unitAddress, float latitude, float longitude,  String description, String tags, String pricing, String openHours, String accommodations, String contact, String grades) {
        this.name = name;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.unitAddress = unitAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.tags = tags;
        this.pricing = pricing;
        this.openHours = openHours;
        this.accommodations = accommodations;
        this.contact = contact;
        this.grades = grades;

    }
}

