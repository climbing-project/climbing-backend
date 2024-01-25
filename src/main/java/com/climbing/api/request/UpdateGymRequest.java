package com.climbing.api.request;

import com.climbing.api.command.UpdateGymCommand;

public class UpdateGymRequest {
    private String name;
    private String address;
    private String description;
    private String tags;
    private String pricings;
    private String openHours;
    private String accommodations;
    private String contacts;
    private String grades;

    public UpdateGymCommand toCommand(Long gymId) {
        return new UpdateGymCommand(gymId, name, address, description, tags, pricings, openHours, accommodations, contacts, grades);
    }
}
