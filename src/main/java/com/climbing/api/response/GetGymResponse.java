package com.climbing.api.response;

import com.climbing.domain.gym.Gym;
import lombok.Getter;

public class GetGymResponse {
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

    private GetGymResponse(String name, String address, String description, String tags, String pricings, String openHours, String accommodations, String contacts, String grades) {
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

    public static GetGymResponse from(Gym gym) {
        return new GetGymResponse(gym.getName(),
                                  gym.getAddress(),
                                  gym.getDescription(),
                                  gym.getTags(),
                                  gym.getPricings(),
                                  gym.getOpenHours(),
                                  gym.getAccommodations(),
                                  gym.getContacts(),
                                  gym.getGrades());
    }
}
