package com.climbing.api.response;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.Gym;
import lombok.Getter;

@Getter
public class UpdateGymResponse {
    private String name;
    private Address address;
    private Coordinates coordinates;
    private String description;
    private String tags;
    private String pricing;
    private String openHours;
    private String accommodations;
    private String contact;
    private String grades;

    private UpdateGymResponse(String name, String jibunAddress, String roadAddress, String unitAddress, float latitude, float longitude, String description, String tags, String pricing, String openHours, String accommodations, String contact, String grades) {
        this.name = name;
        this.address = new Address(jibunAddress, roadAddress, unitAddress);
        this.coordinates = new Coordinates(latitude, longitude);
        this.description = description;
        this.tags = tags;
        this.pricing = pricing;
        this.openHours = openHours;
        this.accommodations = accommodations;
        this.contact = contact;
        this.grades = grades;

    }

    public static UpdateGymResponse from(Gym gym) {
        return new UpdateGymResponse(gym.getName(),
                                     gym.getJibunAddress(),
                                     gym.getRoadAddress(),
                                     gym.getUnitAddress(),
                                     gym.getLatitude(),
                                     gym.getLongitude(),
                                     gym.getDescription(),
                                     gym.getTags(),
                                     gym.getPricing(),
                                     gym.getOpenHours(),
                                     gym.getAccommodations(),
                                     gym.getContact(),
                                     gym.getGrades());
    }
}
