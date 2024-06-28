package com.climbing.api.response;

import com.climbing.domain.gym.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class GetCommentResponse {
    private final Long id;
    private final String user;
    @JsonFormat(shape = Shape.STRING, pattern = "yy.MM.dd", timezone = "Asia/Seoul")
    private final LocalDate createdAt;
    private final String text;


    public GetCommentResponse(Comment comment) {
        this.id = comment.getId();
        this.user = comment.getMember().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.text = comment.getText();
    }

    public static List<GetCommentResponse> from(List<Comment> comments) {
        return comments.stream()
                .map(GetCommentResponse::new)
                .toList();
    }
}
