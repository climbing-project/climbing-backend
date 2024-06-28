package com.climbing.domain.gym;

import com.climbing.api.command.PostCommentCommand;
import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import com.climbing.auth.login.GetLoginMember;
import com.climbing.constant.SortType;
import com.climbing.domain.gym.repository.CommentRepository;
import com.climbing.domain.gym.repository.GymRepository;
import com.climbing.domain.gym.repository.GymTagRepository;
import com.climbing.domain.gym.repository.TagRepository;
import com.climbing.domain.member.Member;
import com.climbing.domain.member.exception.MemberException;
import com.climbing.domain.member.exception.MemberExceptionType;
import com.climbing.domain.member.repository.MemberRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GymService {

    private final int PAGE_SIZE = 12;

    private final GymRepository gymRepository;
    private final TagRepository tagRepository;
    private final GymTagRepository gymTagRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    public GymService(GymRepository gymRepository, TagRepository tagRepository, GymTagRepository gymTagRepository,
                      MemberRepository memberRepository, CommentRepository commentRepository) {
        this.gymRepository = gymRepository;
        this.tagRepository = tagRepository;
        this.gymTagRepository = gymTagRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
    }

    public List<Gym> findGymList() {
        List<Gym> gyms = gymRepository.findAll();
        return gyms;
    }

    public List<Gym> findLatestGym() {
        Pageable pageRequest = getPageRequest(0, 6, SortType.LATE);
        return gymRepository.findAllBy(pageRequest);
    }

    public Gym findGym(Long gymId) {

        Optional<Gym> optionalGym = gymRepository.findById(gymId);
        if (optionalGym.isEmpty()) {
            throw new GymException(GymExceptionType.GYM_NOT_FOUND);
        }
        return optionalGym.get();
    }

    public Long createGym(PostGymCommand command) {
        String email = GetLoginMember.getLoginMemberEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        Gym gym = new Gym(command.getName(),
                command.getAddress().jibunAddress(),
                command.getAddress().roadAddress(),
                command.getAddress().unitAddress(),
                command.getCoordinates().latitude(),
                command.getCoordinates().longitude(),
                command.getContact(),
                member
        );
        return gymRepository.save(gym).getId();
    }

    public void deleteGym(Long gymId) {
        if (!gymRepository.existsById(gymId)) {
            throw new GymException(GymExceptionType.GYM_NOT_FOUND);
        }
        gymRepository.deleteById(gymId);
    }

    public void updateGym(UpdateGymCommand command) {
        Gym gym = gymRepository.findById(command.id())
                .orElseThrow(() -> new GymException(GymExceptionType.GYM_NOT_FOUND));

        List<GymTag> gymTags = gym.getGymTags();
        List<GymTag> gymTagsToDelete = gymTags.stream()
                .filter(o -> !command.tags().contains(o.getTag().getValue()))
                .toList();
        gymTagRepository.deleteAll(gymTagsToDelete);

        List<String> currentTagValues = gymTags.stream()
                .map(o -> o.getTag().getValue())
                .toList();
        List<String> values = command.tags().stream()
                .filter(o -> !currentTagValues.contains(o))
                .toList();
        List<GymTag> gymTagsToAdd = tagRepository.findByValueIn(values).stream()
                .map(tag -> new GymTag(gym, tag))
                .toList();
        List<GymTag> newGymTags = gymTagRepository.saveAll(gymTagsToAdd);

        gym.update(command.name(),
                command.address().jibunAddress(),
                command.address().roadAddress(),
                command.address().unitAddress(),
                command.coordinates().latitude(),
                command.coordinates().longitude(),
                command.contact(),
                command.latestSettingDate(),
                command.sns().twitter(),
                command.sns().facebook(),
                command.sns().instagram(),
                command.homepage(),
                command.images(),
                command.defaultImage(),
                command.openHours(),
                command.pricing(),
                newGymTags,
                command.description(),
                command.grades(),
                command.accommodations()
        );
        gymRepository.save(gym);
    }

    public List<Gym> findGymByNameOrAddress(String keyword, SortType sortType, int pageNum) {
        Pageable pageRequest = getPageRequest(pageNum, PAGE_SIZE, sortType);
        Page<Gym> page = gymRepository.findAllByNameContainsOrJibunAddressContains(keyword, keyword, pageRequest);
        if (page == null || !page.hasContent()) {
            return Collections.emptyList();
        }
        return page.getContent();
    }

    public PageRequest getPageRequest(int pageNum, int pageSize, SortType sortType) {
        Sort sort = switch (sortType) {
            case POPU -> Sort.by("hits").descending();
            case LATE -> Sort.by("latestSettingDate").descending();
            case DIST -> Sort.by("name");//TODO
            case NAME -> Sort.by("name");
        };
        return PageRequest.of(pageNum, pageSize, sort);
    }

    public List<Gym> findMyGym() {
        String email = GetLoginMember.getLoginMemberEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return gymRepository.findAllByMember(member);
    }

    public List<Comment> findComments(Long gymId) {
        return commentRepository.findAllByGymId(gymId);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new GymException(GymExceptionType.COMMENT_NOT_FOUND);
        }
        commentRepository.deleteById(commentId);
    }

    public void validateManageable(Long gymId) {
        String email = GetLoginMember.getLoginMemberEmail();
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new GymException(GymExceptionType.GYM_NOT_FOUND));
        if(!gym.getMember().getEmail().equals(email)) {
            throw new GymException(GymExceptionType.UNAUTHORIZED);
        }
    }

    public void addComment(PostCommentCommand command) {
        String email = GetLoginMember.getLoginMemberEmail();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        Gym gym = gymRepository.findById(command.gymId()).orElseThrow(() -> new GymException(GymExceptionType.GYM_NOT_FOUND));
        Comment comment = new Comment(gym, member, command.text());
        commentRepository.save(comment);
    }

    public void validateCommentWriter(Long commentId) {
        Object email = GetLoginMember.getLoginMemberEmail();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GymException(GymExceptionType.COMMENT_NOT_FOUND));
        if (!comment.getMember().getEmail().equals(email)) {
            throw new GymException(GymExceptionType.UNAUTHORIZED);
        }
    }
}
