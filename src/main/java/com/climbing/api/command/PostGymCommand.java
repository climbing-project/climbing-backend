package com.climbing.api.command;

import lombok.Getter;

public class PostGymCommand {
    @Getter
    private String name;
    @Getter
    private String address;

    public PostGymCommand(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
