package com.climbing.api.controller;

import com.climbing.api.request.AuthorizeRoleRequest;
import com.climbing.api.response.AuthorizeRoleResponse;
import com.climbing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @PutMapping("/members/{member_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorizeRoleResponse> authorizeRole(@PathVariable("member_id") Long memberId, @RequestBody AuthorizeRoleRequest request) {
        AuthorizeRoleResponse response = memberService.authorizeRole(request, memberId);
        return ResponseEntity.ok(response);
    }
}
