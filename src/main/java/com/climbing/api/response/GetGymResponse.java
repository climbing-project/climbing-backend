package com.climbing.api.response;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymTag;
import com.climbing.domain.gym.OpenHour;
import com.climbing.domain.gym.Pricing;
import com.climbing.domain.gym.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class GetGymResponse {
    private String name;
    private Address address;
    private Coordinates coordinates;
    private String description;
    private List<String> tags;
    private List<Pricing> pricing;
    private List<OpenHour> openHours;
    private String accommodations;
    private String contact;
    private List<String> grades;

    private GetGymResponse(String name, String jibunAddress, String roadAddress, String unitAddress, float latitude, float longitude, String description, List<String> tags, List<Pricing> pricing, List<OpenHour> openHours, String accommodations, String contact, List<String> grades) {
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

    public static GetGymResponse from(Gym gym) {
        return new GetGymResponse(gym.getName(),
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
