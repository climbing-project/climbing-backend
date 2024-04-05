package com.climbing.api.command;

import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import lombok.Getter;

@Getter
public class PostGymCommand {
    private String name;
    private Address address;
    private Coordinates coordinates;
    private String contact;


    public PostGymCommand(String name, Address address, Coordinates coordinates, String contact) {
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.contact = contact;
    }
}
