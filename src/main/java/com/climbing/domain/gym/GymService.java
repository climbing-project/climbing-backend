package com.climbing.domain.gym;

import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.climbing.domain.gym.GymException.GymNotFoundException;

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
        if (optionalGym.isEmpty()) {
            throw new GymNotFoundException();
        }
        return optionalGym.get();
    }

    public Long createGym(PostGymCommand command) {
        Gym gym = Gym.of(command.getName(),
                         command.getAddress(),
                         command.getDescription(),
                         command.getTags(),
                         command.getPricings(),
                         command.getOpenHours(),
                         command.getAccommodations(),
                         command.getContacts(),
                         command.getGrades()
        );
        gymRepository.save(gym);
        return 2L;
    }

    public void deleteGym(Long gymId) throws GymNotFoundException {
        if (!gymRepository.existsById(gymId))
            throw new GymNotFoundException();
        gymRepository.deleteById(gymId);
    }

    public Gym updateGym(UpdateGymCommand command) throws GymNotFoundException {
        Optional<Gym> optionalGym = gymRepository.findById(command.getId());
        if (optionalGym.isEmpty()) {
            throw new GymNotFoundException();
        }
        Gym gym = optionalGym.get();
        gym.update(command.getName(),
                   command.getAddress(),
                   command.getDescription(),
                   command.getTags(),
                   command.getPricings(),
                   command.getOpenHours(),
                   command.getAccommodations(),
                   command.getContacts(),
                   command.getGrades());
        return gymRepository.save(gym);
    }
}
