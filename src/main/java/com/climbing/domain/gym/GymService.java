package com.climbing.domain.gym;

import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import com.climbing.constant.SortType;
import com.climbing.domain.gym.repository.GymRepository;
import com.climbing.domain.gym.repository.GymTagRepository;
import com.climbing.domain.gym.repository.TagRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class GymService {

    private static final int pageSize = 20;

    private final GymRepository gymRepository;
    private final TagRepository tagRepository;
    private final GymTagRepository gymTagRepository;

    public GymService(GymRepository gymRepository, TagRepository tagRepository, GymTagRepository gymTagRepository) {
        this.gymRepository = gymRepository;
        this.tagRepository = tagRepository;
        this.gymTagRepository = gymTagRepository;
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
        Gym gym = new Gym(command.getName(),
                command.getAddress().jibunAddress(),
                command.getAddress().roadAddress(),
                command.getAddress().roadAddress(),
                command.getCoordinates().latitude(),
                command.getCoordinates().longitude(),
                command.getContact()
        );
        return gymRepository.save(gym).getId();
    }

    public void deleteGym(Long gymId) {
        if (!gymRepository.existsById(gymId)) {
            throw new GymException(GymExceptionType.GYM_NOT_FOUND);
        }
        gymRepository.deleteById(gymId);
    }

    public Gym updateGym(UpdateGymCommand command) {
        Gym gym = gymRepository.findById(command.id())
                .orElseThrow(() -> new GymException(GymExceptionType.GYM_NOT_FOUND));

        gymTagRepository.deleteAllByGym(gym);
        List<Tag> newTags = tagRepository.findByValueIn(command.tags()); //TODO

        gym.update(command.name(),
                command.address().jibunAddress(),
                command.address().roadAddress(),
                command.address().unitAddress(),
                command.coordinates().latitude(),
                command.coordinates().longitude(),
                command.description(),
                command.pricing(),
                command.openHours(),
                command.accommodations(),
                command.contact(),
                command.grades()
        );
        return gymRepository.save(gym);
    }

    public List<Gym> findQueriedGymList(String address, SortType sortType, int nextIndex) {
        // TODO: address에 따른 쿼리 구현 필요

        List<Long> range = LongStream.range(nextIndex, nextIndex + pageSize).boxed().toList();
        switch (sortType) {
            case POPULAR -> {
                return gymRepository.findByIdInOrderByHitsDesc(range);
            }
            case LATEST -> {
                return gymRepository.findByIdInOrderByCreatedAtDesc(range);
            }
            case DISTANCE -> {
                return gymRepository.findByIdIn(range); // TODO: distance 이용 쿼리 구현 필요
            }
            case NAME -> {
                return gymRepository.findByIdInOrderByNameAsc(range);
            }
            default -> throw new GymException(GymExceptionType.SORT_TYPE_NOT_FOUND);
        }
    }
}
