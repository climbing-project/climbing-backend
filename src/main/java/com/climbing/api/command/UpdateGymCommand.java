package com.climbing.api.command;

import lombok.Getter;

public class UpdateGymCommand {
    @Getter
    private Long id;
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

    public UpdateGymCommand(Long id, String name, String address, String description, String tags, String pricings, String openHours, String accommodations, String contacts, String grades) {
        this.id = id;
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
}
