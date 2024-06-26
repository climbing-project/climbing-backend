package com.climbing.api.bookmark.service;

import com.climbing.api.bookmark.response.BookmarkResponse;
import com.climbing.api.bookmark.response.MyBookmarkResponse;

import java.util.List;

public interface BookmarkService {
    BookmarkResponse createAndDeleteBookmark(Long gymId);

    List<MyBookmarkResponse> searchMyBookmarks();

    boolean checkBookmark(Long gymId);
}
