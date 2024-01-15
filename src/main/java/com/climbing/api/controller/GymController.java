package com.climbing.api.controller;

import com.climbing.api.response.GetGymResponse;
import com.climbing.api.response.GetSimpleGymResponse;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.climbing.domain.gym.GymException.GymNotFoundException;

@RestController
@RequestMapping("/gyms")
public class GymController {
    private GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping
    public List<GetSimpleGymResponse> getGymList() {
        List<Gym> gyms = gymService.findGymList();
        return GetSimpleGymResponse.from(gyms);
    }

    @GetMapping("/{gymId}")
    public GetGymResponse getGym(@PathVariable(name = "gymId") Long gymId) {
        try {
            Gym gym = gymService.findGym(gymId);
            return GetGymResponse.from(gym);
        } catch (GymNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
