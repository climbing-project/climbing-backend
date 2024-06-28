package com.climbing.api.controller;

import com.climbing.api.command.PostGymCommand;
import com.climbing.api.command.UpdateGymCommand;
import com.climbing.api.request.PostCommentRequest;
import com.climbing.api.request.PostGymRequest;
import com.climbing.api.request.UpdateGymRequest;
import com.climbing.api.response.GetGymResponse;
import com.climbing.api.response.GetSimpleGymResponse;
import com.climbing.api.response.PostGymResponse;
import com.climbing.constant.SortType;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        List<Gym> gyms = gymService.findLatestGym();
        return new ResponseEntity<>(GetSimpleGymResponse.from(gyms), HttpStatus.OK);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<GetSimpleGymResponse>> searchGymByName(
            @RequestParam(name = "q") String keyword,
            @RequestParam(name = "s", required = false, defaultValue = "NAME") SortType sortType,
            @RequestParam(name = "p", required = false, defaultValue = "0") int pageNum) {
        List<Gym> gymList = gymService.findGymByNameOrAddress(keyword, sortType, pageNum);
        return new ResponseEntity<>(GetSimpleGymResponse.from(gymList), HttpStatus.OK);
    }

    @GetMapping("/{gymId}")
    public ResponseEntity<GetGymResponse> getGym(@PathVariable(name = "gymId") Long gymId) {
        Gym gym = gymService.findGym(gymId);
        GetGymResponse from = GetGymResponse.from(gym);
        return new ResponseEntity<>(from, HttpStatus.OK);
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN'||'MANAGER')")
    public ResponseEntity<PostGymResponse> postGym(@RequestBody PostGymRequest request) {
        PostGymCommand command = request.toCommand();
        Long gymId = gymService.createGym(command);
        return new ResponseEntity<>(PostGymResponse.from(gymId), HttpStatus.OK);
    }

    @DeleteMapping("/{gymId}")
    public ResponseEntity<?> deleteGym(@PathVariable(value = "gymId") Long gymId) {
        gymService.deleteGym(gymId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{gymId}")
    public ResponseEntity<?> updateGym(@PathVariable(value = "gymId") Long gymId,
                                       @RequestBody UpdateGymRequest request) {
        UpdateGymCommand command = request.toCommand(gymId);
        gymService.updateGym(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{gymId}/comments")
    public ResponseEntity<?> postComment(@PathVariable(value = "gymId") Long gymId,
                                         @RequestBody PostCommentRequest request) {
        gymService.addComment(request.toCommand(gymId));
        return ResponseEntity.ok().build();
    }
}
