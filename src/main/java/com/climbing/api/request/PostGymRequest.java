package com.climbing.api.request;

import com.climbing.api.command.PostGymCommand;
import com.climbing.domain.gym.Address;
import com.climbing.domain.gym.Coordinates;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostGymRequest {
    private String name;
    private Address address;
    private Coordinates coordinates;
    private String contact;

    public PostGymCommand toCommand() {
        return new PostGymCommand(name, address, coordinates, contact);
    }
}
