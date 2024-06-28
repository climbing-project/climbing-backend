package com.climbing.api.bookmark.response;

import com.climbing.api.bookmark.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponse {
    private String message;
    private boolean status;

    public static BookmarkResponse of(String message, Bookmark bookmark) {
        return new BookmarkResponse(
                message,
                bookmark.isStatus()
        );
    }
}
