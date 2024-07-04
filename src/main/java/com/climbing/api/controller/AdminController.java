package com.climbing.api.controller;

import com.climbing.api.request.AuthorizeRoleRequest;
import com.climbing.api.response.AuthorizeRoleResponse;
import com.climbing.api.response.GetMemberListResponse;
import com.climbing.domain.member.Role;
import com.climbing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

//    @GetMapping("/members")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<GetMemberListResponse>> getMemberList() {
//        List<GetMemberListResponse> responses = memberService.findAllMembers();
//        return ResponseEntity.ok(responses);
//    }

    @GetMapping("/members")
    public ResponseEntity<Page<GetMemberListResponse>> getMemberListPage(
            @RequestParam(value = "r", defaultValue = "null") Role r,
            @RequestParam(value = "p", defaultValue = "1") int p,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "s", defaultValue = "ASC") String s) {
        Sort sort = s.equalsIgnoreCase("DESC") ? Sort.by("nickname").descending() : Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(p - 1, size, sort);
        Page<GetMemberListResponse> responses = memberService.findAllMembersPage(pageable);
        if (r == null) {
            responses = memberService.findMembersByRole(r, pageable);
        }
        return ResponseEntity.ok(responses);
    }
}
