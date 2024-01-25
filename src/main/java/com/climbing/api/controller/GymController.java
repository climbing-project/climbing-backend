package com.climbing.api.controller;

import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import com.climbing.api.request.PostGymRequest;
import com.climbing.api.request.UpdateGymRequest;
import com.climbing.api.response.*;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<GetSimpleGymResponse>> getGymList() {
        List<Gym> gyms = gymService.findGymList();
        return new ResponseEntity<>(GetSimpleGymResponse.from(gyms), HttpStatus.OK);
    }

    @GetMapping("/{gymId}")
    public ResponseEntity<GetGymResponse> getGym(@PathVariable(name = "gymId") Long gymId) {
        try {
            Gym gym = gymService.findGym(gymId);
            return new ResponseEntity<>(GetGymResponse.from(gym), HttpStatus.OK);
        } catch (GymNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<PostGymResponse> postGym(@RequestBody PostGymRequest request) {
        PostGymCommand command = request.toCommand();
        Long gymId = gymService.createGym(command);
        return new ResponseEntity<>(PostGymResponse.from(gymId), HttpStatus.OK);
    }

    @DeleteMapping("/{gymId}")
    public ResponseEntity<Responsible> deleteGym(@PathVariable(value = "gymId") Long gymId) {
        try {
            gymService.deleteGym(gymId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (GymNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{gymId}")
    public ResponseEntity<UpdateGymResponse> updateGym(@PathVariable(value = "gymId") Long gymId,
                                                       @RequestBody UpdateGymRequest request) {
        UpdateGymCommand command = request.toCommand(gymId);
        try {
            Gym gym = gymService.updateGym(command);
            return new ResponseEntity<>(UpdateGymResponse.from(gym), HttpStatus.OK);
        } catch (GymNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
