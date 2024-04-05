package com.climbing.domain.gym;

import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import com.climbing.constant.SortType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

@Transactional
@Service
public class GymService {

    private static final int pageSize = 20;

    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
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
        Gym gym = Gym.of(command.getName(),
                         command.getAddress().jibunAddress(),
                         command.getAddress().roadAddress(),
                         command.getAddress().unitAddress(),
                         command.getCoordinates().latitude(),
                         command.getCoordinates().longitude(),
                         null,
                         null,
                         null,
                         null,
                         null,
                         command.getContact(),
                         null
        );
        return gymRepository.save(gym).getId();
    }

    public void deleteGym(Long gymId) {
        if (!gymRepository.existsById(gymId))
            throw new GymException(GymExceptionType.GYM_NOT_FOUND);
        gymRepository.deleteById(gymId);
    }

    public Gym updateGym(UpdateGymCommand command) {
        Optional<Gym> optionalGym = gymRepository.findById(command.getId());
        if (optionalGym.isEmpty()) {
            throw new GymException(GymExceptionType.GYM_NOT_FOUND);
        }
        Gym gym = optionalGym.get();
        gym.update(command.getName(),
                   command.getAddress().jibunAddress(),
                   command.getAddress().roadAddress(),
                   command.getAddress().unitAddress(),
                   command.getCoordinates().latitude(),
                   command.getCoordinates().longitude(),
                   command.getDescription(),
                   command.getTags(),
                   command.getPricing(),
                   command.getOpenHours(),
                   command.getAccommodations(),
                   command.getContact(),
                   command.getGrades());
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
