package com.climbing.api.command;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import lombok.Getter;

@Getter
public class UpdateGymCommand {
    private Long id;
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

    public UpdateGymCommand(Long id, String name, Address address, Coordinates coordinates, String description, String tags, String pricing, String openHours, String accommodations, String contact, String grades) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.description = description;
        this.tags = tags;
        this.pricing = pricing;
        this.openHours = openHours;
        this.accommodations = accommodations;
        this.contact = contact;
        this.grades = grades;
    }
}
