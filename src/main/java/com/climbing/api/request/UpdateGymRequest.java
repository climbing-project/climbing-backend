package com.climbing.api.request;

import com.climbing.api.command.UpdateGymCommand;
import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import lombok.Getter;

@Getter
public class UpdateGymRequest {
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

    public UpdateGymCommand toCommand(Long gymId) {
        return new UpdateGymCommand(gymId, name, address, coordinates, description, tags, pricing, openHours, accommodations, contact, grades);
    }
}
