package com.skillswap.websocketconfig;

import com.skillswap.SpringSecurity.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        System.out.println("Class JwtChannelInterceptor loaded");
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(message);

        System.out.println("COMMAND = " + accessor.getCommand());

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String auth =
                    accessor.getFirstNativeHeader("Authorization");

            System.out.println("AUTH HEADER = " + auth);

            if (auth == null || !auth.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing JWT");
            }

            String token = auth.substring(7);

            String username =
                    jwtUtil.extractUserId(token);

            System.out.println("Token extracted = " + token);
            System.out.println("Username = " + username);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of()
                    );

            accessor.setUser(authentication);

            System.out.println(
                    "CONNECT USER = " +
                            accessor.getUser()
            );
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {

            System.out.println(
                    "SUBSCRIBE DESTINATION = " +
                            accessor.getDestination()
            );

            System.out.println(
                    "SUBSCRIBE USER = " +
                            accessor.getUser()
            );
        }

        if (StompCommand.SEND.equals(accessor.getCommand())) {

            System.out.println(
                    "SEND DESTINATION = " +
                            accessor.getDestination()
            );

            System.out.println(
                    "SEND USER = " +
                            accessor.getUser()
            );
        }

        return message;
    }
}