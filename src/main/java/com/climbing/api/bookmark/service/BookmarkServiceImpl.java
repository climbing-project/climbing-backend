package com.climbing.api.bookmark.service;

import com.climbing.api.bookmark.Bookmark;
import com.climbing.api.bookmark.repository.BookmarkRepository;
import com.climbing.api.bookmark.response.BookmarkResponse;
import com.climbing.api.bookmark.response.MyBookmarkResponse;
import com.climbing.auth.login.GetLoginMember;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymException;
import com.climbing.domain.gym.GymExceptionType;
import com.climbing.domain.gym.repository.GymRepository;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.exception.MemberException;
import com.climbing.domain.member.exception.MemberExceptionType;
import com.climbing.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final GymRepository gymRepository;
    private final MemberRepository memberRepository;

    @Override
    public BookmarkResponse createAndDeleteBookmark(Long gymId) {
        String email = GetLoginMember.getLoginMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        Long memberId = member.getId();
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new GymException(GymExceptionType.GYM_NOT_FOUND));
        if (bookmarkRepository.findByMemberIdAndGymId(memberId, gymId) == null) {
            Bookmark bookmark = Bookmark.of(member, gym);
            bookmarkRepository.save(bookmark);
            return BookmarkResponse.of("북마크가 설정되었습니다.", bookmark);
        } else {
            Bookmark bookmark = bookmarkRepository.findByMemberIdAndGymId(memberId, gymId);
            bookmark.deleteBookmark();
//            bookmarkRepository.deleteByMemberIdAndGymId(memberId, gymId);
            return BookmarkResponse.of("북마크가 취소되었습니다.", bookmark);
        }
    }

    public boolean checkBookmark(Long gymId) {
        String email = GetLoginMember.getLoginMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        Long memberId = member.getId();
        if (bookmarkRepository.findByMemberIdAndGymId(memberId, gymId) == null) {
            return false;
        } else {
            Bookmark bookmark = bookmarkRepository.findByMemberIdAndGymId(memberId, gymId);
            return bookmark.isStatus();
        }
    }

    public List<MyBookmarkResponse> searchMyBookmarks() {
        String email = GetLoginMember.getLoginMemberEmail();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        Long memberId = member.getId();
        return bookmarkRepository.findAllByMemberId(memberId).stream().map(bookmark -> MyBookmarkResponse.of(bookmark.getGym()))
                .collect(Collectors.toList());
    }
}
