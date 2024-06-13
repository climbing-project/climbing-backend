package com.climbing.api.response;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.OpenHour;
import com.climbing.domain.gym.Pricing;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateGymResponse {
    private String name;
    private Address address;
    private Coordinates coordinates;
    private String contact;
    private String description;
    private List<String> tags;
    private List<Pricing> pricing;
    private List<OpenHour> openHours;
    private List<String> accommodations;
    private List<String> grades;

    private UpdateGymResponse(String name, String jibunAddress, String roadAddress, String unitAddress, float latitude,
                              float longitude, String description, List<String> tags, List<Pricing> pricing,
                              List<OpenHour> openHours, List<String> accommodations, String contact, List<String> grades) {
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
                gym.getGymTags().stream().map(o -> o.getTag().getValue()).toList(),
                gym.getPricing(),
                gym.getOpenHours(),
                gym.getAccommodations(),
                gym.getContact(),
                gym.getGrades());
    }
}
