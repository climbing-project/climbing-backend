package com.climbing.domain.gym;

import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import com.climbing.auth.login.GetLoginMember;
import com.climbing.constant.SortType;
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

    private static final int pageSize = 20;

    private final GymRepository gymRepository;
    private final TagRepository tagRepository;
    private final GymTagRepository gymTagRepository;
    private final MemberRepository memberRepository;

    public GymService(GymRepository gymRepository, TagRepository tagRepository, GymTagRepository gymTagRepository,
                      MemberRepository memberRepository) {
        this.gymRepository = gymRepository;
        this.tagRepository = tagRepository;
        this.gymTagRepository = gymTagRepository;
        this.memberRepository = memberRepository;
    }

    public List<Gym> findGymList() {
        List<Gym> gyms = gymRepository.findAll();
        return gyms;
    }

    public Gym findGym(Long gymId) {

        Optional<Gym> optionalGym = gymRepository.findById(gymId);
        if (optionalGym.isEmpty()) {
            throw new GymException(GymExceptionType.GYM_NOT_FOUND);
        }
        return optionalGym.get();
    }

    public Long createGym(PostGymCommand command) {
        Member member = memberRepository.findByEmail(GetLoginMember.getLoginMemberEmail()).orElseThrow(
                () -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        Gym gym = new Gym(command.getName(),
                command.getAddress().jibunAddress(),
                command.getAddress().roadAddress(),
                command.getAddress().roadAddress(),
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

    public List<Gym> findGymByAddress(String address, SortType sortType, int pageNum) {
        Pageable pageRequest = getPageRequest(pageNum, pageSize, sortType);
        Page<Gym> page = gymRepository.findAllByJibunAddressStartsWith(address, pageRequest);
        if (page == null || !page.hasContent()) {
            return Collections.emptyList();
        }
        return page.getContent();
    }

    public List<Gym> findGymByName(String name, SortType sortType, int pageNum) {
        Pageable pageRequest = getPageRequest(pageNum, pageSize, sortType);
        Page<Gym> page = gymRepository.findAllByNameContains(name, pageRequest);
        if (page == null || !page.hasContent()) {
            return Collections.emptyList();
        }
        return page.getContent();
    }

    public PageRequest getPageRequest(int pageNum, int pageSize, SortType sortType) {
        Sort sort = switch (sortType) {
            case POPULAR -> Sort.by("hits").descending();
            case LATEST -> Sort.by("latestSettingDate").descending();
            case DISTANCE -> Sort.by("name");
            case NAME -> Sort.by("name");
        };
        return PageRequest.of(pageNum, pageSize, sort);
    }
}
