package com.climbing.auth.email.service;

import com.climbing.auth.email.EmailInfo;

public interface EmailService {

    String writeContent(String authNum, String type);

    String sendEmail(EmailInfo emailInfo, String type);

    String makeAuthNum();

    String makeTempPassword();
    
}
