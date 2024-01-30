package com.climbing.auth.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetLoginMember {
    public static String getLoginMemberEmail() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        return loggedInUser.getName();
    }
}
