package com.climbing.domain.gym;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.climbing.domain.gym.GymException.*;

@Service
public class GymService {
    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    public List<Gym> findGymList() {
        List<Gym> gyms = gymRepository.findAll();
        return gyms;
    }

    public Gym findGym(Long gymId) throws GymNotFoundException {
        Optional<Gym> optionalGym = gymRepository.findById(gymId);
        if(optionalGym.isEmpty())
            throw new GymNotFoundException();
        return optionalGym.get();
    }
}
