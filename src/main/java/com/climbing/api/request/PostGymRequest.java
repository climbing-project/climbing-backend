package com.climbing.api.request;

import com.climbing.api.command.PostGymCommand;

public class PostGymRequest {
    private String name;

    private String address;

    public PostGymCommand toCommand() {
        return new PostGymCommand(name, address);
    }
}
