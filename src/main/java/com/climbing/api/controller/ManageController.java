package com.climbing.api.controller;

import com.climbing.api.response.GetCommentResponse;
import com.climbing.api.response.GetSimpleGymResponse;
import com.climbing.domain.gym.Comment;
import com.climbing.domain.gym.Gym;
import com.climbing.domain.gym.GymService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage")
@PreAuthorize("hasRole('MANAGER')")
public class ManageController {
    private final GymService gymService;

    public ManageController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping("/gyms")
    public ResponseEntity<List<GetSimpleGymResponse>> getManageGymList() {
        List<Gym> gyms = gymService.findMyGym();
        return ResponseEntity.ok(GetSimpleGymResponse.from(gyms));
    }

    @GetMapping("/gyms/{gymId}/comments")
    public ResponseEntity<List<GetCommentResponse>> getComments(@PathVariable(value = "gymId") Long gymId) {
        gymService.validateManageable(gymId);
        List<Comment> comments = gymService.findComments(gymId);
        return ResponseEntity.ok(GetCommentResponse.from(comments));
    }

    @DeleteMapping("/gyms/{gymId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "gymId") Long gymId,
                                           @PathVariable(value = "commentId") Long commentId) {
        gymService.validateManageable(gymId);
        gymService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }
}
