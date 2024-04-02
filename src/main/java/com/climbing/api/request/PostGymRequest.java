package com.climbing.api.request;

import com.climbing.api.command.PostGymCommand;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostGymRequest {
    private String name;
    private String address;
    private String description;
    private String tags;
    private String pricings;
    private String openHours;
    private String accommodations;
    private String contacts;
    private String grades;

    public PostGymCommand toCommand() {
        return new PostGymCommand(name, address, description, tags, pricings, openHours, accommodations, contacts, grades);
    }
}
