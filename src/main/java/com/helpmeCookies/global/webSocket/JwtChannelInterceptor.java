package com.helpmeCookies.global.webSocket;


import com.helpmeCookies.global.jwt.JwtProvider;
import com.helpmeCookies.global.jwt.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    public JwtChannelInterceptor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            Optional<String> jwtTokenOptional = Optional.ofNullable(accessor.getFirstNativeHeader("Authorization"));
            String jwtToken = jwtTokenOptional
                    .filter(token -> token.startsWith("Bearer "))
                    .map(token -> token.substring(7))
                    .orElseThrow(() -> new RuntimeException("Invalid token"));

            // validateToken을 사용하여 토큰이 유효하고 만료되지 않았는지 확인
            if (jwtProvider.validateToken(jwtToken, true)) {
                // getJwtUser로 사용자 정보를 가져오고, Authentication 객체로 변환
                JwtUser jwtUser = jwtProvider.getJwtUser(jwtToken);
                Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
                accessor.setUser(authentication);
            } else {
                throw new RuntimeException("Token has expired or is invalid");
            }
        }

        return message;
    }
}

