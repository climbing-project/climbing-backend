package com.climbing.api.controller;

import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import com.climbing.api.request.PostGymRequest;
import com.climbing.api.request.UpdateGymRequest;
import com.climbing.api.response.BasicResponse;
import com.climbing.api.response.GetGymResponse;
import com.climbing.api.response.GetSimpleGymResponse;
import com.climbing.api.response.PostGymResponse;
import com.climbing.api.response.UpdateGymResponse;
import com.climbing.constant.SortType;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gyms")
public class GymController {
    private final GymService gymService;

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
        Gym gym = gymService.findGym(gymId);
        GetGymResponse from = GetGymResponse.from(gym);
        return new ResponseEntity<>(from, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN'||'MANAGER')")
    public ResponseEntity<PostGymResponse> postGym(@RequestBody PostGymRequest request) {
        PostGymCommand command = request.toCommand();
        Long gymId = gymService.createGym(command);
        return new ResponseEntity<>(PostGymResponse.from(gymId), HttpStatus.OK);
    }

    @DeleteMapping("/{gymId}")
    public ResponseEntity<BasicResponse> deleteGym(@PathVariable(value = "gymId") Long gymId) {
        gymService.deleteGym(gymId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{gymId}")
    public ResponseEntity<UpdateGymResponse> updateGym(@PathVariable(value = "gymId") Long gymId,
                                                       @RequestBody UpdateGymRequest request) {
        UpdateGymCommand command = request.toCommand(gymId);
        gymService.updateGym(command);
//        return new ResponseEntity<>(UpdateGymResponse.from(gym), HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping(path = "/search", params = {"gym_name", "s", "p"})
    public ResponseEntity<List<GetSimpleGymResponse>> searchGymByName(
            @RequestParam(name = "gym_name") String gymName,
            @RequestParam(name = "s", required = false) SortType sortType,
            @RequestParam(name = "p", defaultValue = "0") int pageNum) {
        List<Gym> gymList = gymService.findGymByName(gymName, sortType, pageNum);
        return new ResponseEntity<>(GetSimpleGymResponse.from(gymList), HttpStatus.OK);
    }

    @GetMapping(path = "/search", params = {"q", "s", "p"})
    public ResponseEntity<List<GetSimpleGymResponse>> searchGymByAddress(
            @RequestParam(name = "q") String address,
            @RequestParam(name = "s", required = false) SortType sortType,
            @RequestParam(name = "p", defaultValue = "0") int pageNum) {
        List<Gym> gymList = gymService.findGymByAddress(address, sortType, pageNum);
        return new ResponseEntity<>(GetSimpleGymResponse.from(gymList), HttpStatus.OK);
    }
}
