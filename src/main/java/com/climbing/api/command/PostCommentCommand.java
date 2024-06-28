package com.climbing.api.command;

public record PostCommentCommand(Long gymId, String text) {
}
