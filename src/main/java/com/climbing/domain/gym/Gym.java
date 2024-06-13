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
    @Convert(converter = JsonConverter.class)
    private List<String> images;
    @Convert(converter = JsonConverter.class)
    private List<Pricing> pricing;
    @Convert(converter = JsonConverter.class)
    private List<OpenHour> openHours;
    @Convert(converter = JsonConverter.class)
    private List<String> accommodations;
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
                       float longitude, String contact, LocalDate latestSettingDate, String twitter, String facebook,
                       String instagram, String homepage, List<String> images, String defaultImage,
                       List<OpenHour> openHours, List<Pricing> pricing, List<GymTag> gymTags, String description,
                       List<String> grades, List<String> accommodations, List<Comment> comments) {
        this.name = name;
        this.jibunAddress = jibunAddress;
        this.roadAddress = roadAddress;
        this.unitAddress = unitAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.contact = contact;
        this.latestSettingDate = latestSettingDate;
        this.twitter = twitter;
        this.facebook = facebook;
        this.instagram = instagram;
        this.homepage = homepage;
        this.images = null;
//        this.defaultImage = "TODO";

        this.description = description;
        this.pricing = pricing;
        this.openHours = openHours;
        this.accommodations = accommodations;
        this.grades = grades;
        this.gymTags = gymTags;
    }
}
