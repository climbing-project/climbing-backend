package com.climbing.auth.email.service;

import com.climbing.auth.email.EmailInfo;
import com.climbing.domain.member.repository.MemberRepository;
import com.climbing.domain.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    public String writeContent(String authNum, String type) {
        String content;

        content = "<h2>오르리에 방문해주셔서 감사합니다.</h2>" +
                "<h3> 이메일 인증번호는 아래와 같습니다 </h3>" +
                "<h2>" + authNum + "</h2>" +
                "<h3> 회원가입 페이지에서 인증번호를 입력해주세요.</h3>";

        if (type.equals("password")) {
            content = "<h2>오르리에 방문해주셔서 감사합니다.</h2>" +
                    "<h3> 임시 비밀번호는 아래와 같습니다 </h3>" +
                    "<h2>" + authNum + "</h2>" +
                    "<h3> 위의 비밀번호로 사용자의 비밀번호가 변경되었습니다.</h3>";
        }
        return content;
    }

    @Override
    public String writeJoinContent(String receiver) {
        String content;

        content = "<h2>오르리에 방문해주셔서 감사합니다</h2>" +
                "<h3> 회원가입이 완료되었습니다. </h3>" +
                "<h2> 회원 가입 이메일은 아래와 같습니다. </h2>" +
                "<h2>" + receiver + "</h2>";

        return content;
    }

    @Override
    public String writeWithdrawContent(String receiver) {
        String content;

        content = "<h2>오르리 회원탈퇴가 완료되었습니다.</h2>" +
                "<h3> 오르리 서비스를 이용해주셔서 감사합니다. </h3>" +
                "<h2> 회원 탈퇴 이메일은 아래와 같습니다. </h2>" +
                "<h2>" + receiver + "</h2>";

        return content;
    }

    @Override
    public String sendEmail(EmailInfo emailInfo, String type) {
        String authNum;

        authNum = makeAuthNum();

        if (type.equals("password")) {
            authNum = makeTempPassword();
            memberService.setTempPassword(emailInfo.getReceiver(), authNum);
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailInfo.getReceiver());
            mimeMessageHelper.setSubject(emailInfo.getTitle());
            mimeMessageHelper.setText(writeContent(authNum, type), true);
            mailSender.send(mimeMessage);
            log.info("이메일 인증용 메일 전송 완료");
            return authNum;
        } catch (MessagingException e) {
            log.info("이메일 인증용 메일 전송 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendJoinEmail(EmailInfo emailInfo) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailInfo.getReceiver());
            mimeMessageHelper.setSubject(emailInfo.getTitle());
            mimeMessageHelper.setText(writeJoinContent(emailInfo.getReceiver()), true);
            mailSender.send(mimeMessage);
            log.info("이메일 회원가입 승인 메일 전송 완료");
        } catch (MessagingException e) {
            log.info("이메일 회원가입 승인 메일 전송 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendWithdrawEmail(EmailInfo emailInfo) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(emailInfo.getReceiver());
            mimeMessageHelper.setSubject(emailInfo.getTitle());
            mimeMessageHelper.setText(writeWithdrawContent(emailInfo.getReceiver()), true);
            mailSender.send(mimeMessage);
            log.info("이메일 회원탈퇴 승인 메일 전송 완료");
        } catch (MessagingException e) {
            log.info("이메일 회원탈퇴 승인 메일 전송 실패");
            throw new RuntimeException(e);
        }
    }

    @Override
    public String makeAuthNum() {
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            buffer.append(random.nextInt(9));
        }
        return buffer.toString();
    }

    @Override
    public String makeTempPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        int length = 8;

        int rndAllCharactersLength = rndAllCharacters.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(rndAllCharacters[random.nextInt(rndAllCharactersLength)]);
        }

        String randomPassword = stringBuilder.toString();

        // 최소 8자리에 대문자, 소문자, 숫자, 특수문자 각 1개 이상 포함
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}";
        if (!Pattern.matches(pattern, randomPassword)) {
            return makeTempPassword();
        }
        return randomPassword;
    }

    private static final char[] rndAllCharacters = new char[]{
            //number
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            //uppercase
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            //lowercase
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            //special symbols
            '@', '$', '!', '%', '*', '?', '&'
    };
}
