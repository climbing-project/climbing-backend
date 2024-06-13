package com.climbing.api.request;


import com.climbing.api.command.PostTagCommand;

public record PostTagRequest(String tag) {
    public PostTagCommand toCommand() {
        return new PostTagCommand(tag);
    }
}
