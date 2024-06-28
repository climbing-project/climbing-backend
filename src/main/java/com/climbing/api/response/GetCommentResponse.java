package com.climbing.api.response;

import com.climbing.domain.gym.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.util.List;

public record GetCommentResponse(Long id,
                                 String user,
                                 @JsonFormat(shape = Shape.STRING, pattern = "yy.MM.dd", timezone = "Asia/Seoul") LocalDate createdAt,
                                 String text) {
    public static List<GetCommentResponse> from(List<Comment> comments) {
        return comments.stream()
                .map(o -> new GetCommentResponse(o.getId(), o.getMember().getNickname(), o.getCreatedAt(), o.getText()))
                .toList();
    }
}
