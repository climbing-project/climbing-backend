package com.climbing.domain.member;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class GetLoginMember {
    public static String getLoginMemberEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        return ((UserDetails) principal).getUsername();
    }
}
