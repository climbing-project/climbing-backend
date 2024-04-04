package com.climbing.api.chat;

import com.climbing.auth.jwt.JwtService;
import com.climbing.domain.member.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ChatInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    String memberEmail = null;
    String token = null;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            String autHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));
            StompCommand command = headerAccessor.getCommand();

            if (command.equals(StompCommand.CONNECTED) || command.equals(StompCommand.SEND) ||
                    command.equals(StompCommand.MESSAGE) || command.equals(StompCommand.SUBSCRIBE)) {
                return message;
            } else if (command.equals(StompCommand.ERROR)) {
                throw new MessageDeliveryException("Chat Message Error");
            }

            if (autHeader == null) {
                log.info("채팅에 올바른 토큰이 존재하지 않습니다.");
                throw new JwtException("JWT Error");
            }

            String authorizationHeaderStr = autHeader.replace("[", "").replace("]", "");
            if (authorizationHeaderStr.startsWith("Bearer ")) {
                token = authorizationHeaderStr.replace("Bearer ", "");
            } else {
                log.error("Authorization 헤더가 올바르지 않습니다. : {}", autHeader);
                throw new JwtException("JWT Error");
            }

            memberEmail = String.valueOf(jwtService.extractEmail(token));

            boolean isTokenValid = jwtService.isTokenValid(token);

            if (isTokenValid) {
                this.setAuthentication(headerAccessor);
            }

        } catch (JwtException e) {
            log.error("JWT 토큰 에러 발생");
            throw new JwtException("JWT Error");
        } catch (MessageDeliveryException e) {
            log.error("채팅 메시지관련 에러 발생");
            throw new MessageDeliveryException("Chat Message Error");
        }
        return message;
    }

    private void setAuthentication(StompHeaderAccessor headerAccessor) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberEmail, null, List.of(new SimpleGrantedAuthority(Role.USER.getKey())));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        headerAccessor.setUser(authenticationToken);
    }
}
