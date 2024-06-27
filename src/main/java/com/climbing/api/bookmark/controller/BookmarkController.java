package com.climbing.api.bookmark.controller;

import com.climbing.api.bookmark.response.BookmarkResponse;
import com.climbing.api.bookmark.response.MyBookmarkResponse;
import com.climbing.api.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/gyms/{gymId}/bookmark")
    public ResponseEntity<BookmarkResponse> createBookmark(@PathVariable("gymId") Long gymId) {
        BookmarkResponse bookmark = bookmarkService.createAndDeleteBookmark(gymId);
        return ResponseEntity.ok(bookmark);
    }

    @GetMapping("/members/bookmark")
    public ResponseEntity<List<MyBookmarkResponse>> myBookmarks() {
        List<MyBookmarkResponse> bookmarks = bookmarkService.searchMyBookmarks();
        return ResponseEntity.ok(bookmarks);
    }

    @GetMapping("/gyms/{gymId}/check/bookmark")
    public ResponseEntity<Boolean> checkBookmark(@PathVariable("gymId") Long gymId) {
        boolean check = bookmarkService.checkBookmark(gymId);
        return ResponseEntity.ok(check);
    }
}
