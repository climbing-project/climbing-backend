package com.climbing.domain.gym;

import org.springframework.stereotype.Service;

@Service
public class GymService {
    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }
}
