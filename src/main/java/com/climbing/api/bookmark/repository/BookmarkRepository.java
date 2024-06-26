package com.climbing.api.bookmark.repository;

import com.climbing.api.bookmark.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Bookmark findByMemberIdAndGymId(Long memberId, Long gymId);

    void deleteByMemberIdAndGymId(Long memberId, Long gymId);

    List<Bookmark> findAllByMemberId(Long memberId);
}
