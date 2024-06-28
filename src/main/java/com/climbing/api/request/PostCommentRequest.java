package com.climbing.api.request;

import com.climbing.api.command.PostCommentCommand;

public record PostCommentRequest(String text) {

    public PostCommentCommand toCommand(Long gymId) {
        return new PostCommentCommand(gymId, text);
    }
}
