package com.climbing.domain.gym;

import com.climbing.api.chat.ChatRoom;
import com.climbing.domain.gym.converter.JsonConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "gym")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String jibunAddress;
    private String roadAddress;
    private String unitAddress;
    private float latitude;
    private float longitude;
    private String description;
    private LocalDate latestSettingDate;
    private String twitter;
    private String facebook;
    private String instagram;
    private String homepage;
    private String images;
    @Convert(converter = JsonConverter.class)
    private List<Pricing> pricing;
    @Convert(converter = JsonConverter.class)
    private List<OpenHour> openHours;
    private String accommodations;
    private String contact;
    @Convert(converter = JsonConverter.class)
    private List<String> grades;
    private String comments;
    private int likes;
    private int hits;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    @OneToMany(mappedBy = "gym")
    private List<ChatRoom> chatRooms;
    @OneToMany(mappedBy = "gym")
    private List<GymTag> gymTags;

    public Gym(String name, String jibunAddress, String roadAddress, String unitAddress, float latitude,
               float longitude, String contact) {
        this.name = name;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.unitAddress = unitAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contact = contact;
    }

    public void update(String name, String jibunAddress, String roadAddress, String unitAddress, float latitude,
                       float longitude, String description, List<Pricing> pricing, List<OpenHour> openHours,
                       String accommodations, String contact, List<String> grades) {
        this.name = name;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.unitAddress = unitAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.pricing = pricing;
        this.openHours = openHours;
        this.accommodations = accommodations;
        this.contact = contact;
        this.grades = grades;
    }
}
