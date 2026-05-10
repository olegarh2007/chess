package com.olegator.chess.interceptor;

import com.olegator.chess.service.ChatService;
import com.olegator.chess.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class StompAuthInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ChatService chatService;

    @Override
    public @Nullable Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            throw new IllegalStateException("null accessor");
        }
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            if (accessor.getUser() != null) {
                return message;
            }
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("123");
            }
            String jwt = authHeader.substring(7);
            String username = jwtService.extractUsername(jwt);

            if (username != null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    accessor.setUser(authToken);

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
            Principal principal = accessor.getUser();
            String destination = accessor.getDestination();

            if (principal == null || destination == null) {
                throw new IllegalStateException("Missing principal or destination");
            }

            if (destination.startsWith("/topic/chat") || destination.startsWith("/app/chat")) {
                Long chatId = extractChatId(destination);
                if (!chatService.isMemberByEmail(principal.getName(), chatId)) {
                    throw new IllegalArgumentException("User is not a member of this chat");
                }
            }
        }
        return message;
    }

    private Long extractChatId(String destination) {
        if (destination == null || !destination.contains(".")) {
            throw new IllegalArgumentException("Invalid destination format");
        }
        try {
            String idPart = destination.substring(destination.lastIndexOf('.') + 1);
            return Long.parseLong(idPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid chat ID in destination", e);
        }
    }
}
