package com.olegator.chess.integration;

import com.olegator.chess.annotation.IT;
import com.olegator.chess.interceptor.StompAuthInterceptor;
import com.olegator.chess.service.ChatService;
import com.olegator.chess.service.JwtService;
import com.olegator.chess.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@IT
@Transactional
class StompAuthInterceptorTest {

	@Autowired
	private StompAuthInterceptor stompAuthInterceptor;

	@MockitoBean
	private JwtService jwtService;

	@MockitoBean
	private UserDetailsService userDetailsService;

	@MockitoBean
	private ChatService chatService;

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void connect_shouldAuthenticateUserFromJwt() {
		UserDetailsImpl userDetails = new UserDetailsImpl(
				1L, "alice@mail.com", "password", Set.of()
		);
		doReturn("alice@mail.com").when(jwtService).extractUsername("token-123");
		doReturn(userDetails).when(userDetailsService).loadUserByUsername("alice@mail.com");
		doReturn(true).when(jwtService).isTokenValid("token-123", userDetails);

		Message<byte[]> message = stompMessage(StompCommand.CONNECT, null, "Bearer token-123", null);
		MessageChannel channel = mock(MessageChannel.class);

		Message<?> result = stompAuthInterceptor.preSend(message, channel);

		assertSame(message, result);
		verify(userDetailsService).loadUserByUsername("alice@mail.com");
		verify(jwtService).isTokenValid("token-123", userDetails);
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		assertNotNull(authentication);
		assertSame(userDetails, authentication.getPrincipal());
	}

	@Test
	void connect_shouldReturnMessageWhenUserAlreadySet() {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				"alice@mail.com",
				null,
				List.of()
		);

		Message<byte[]> message = stompMessage(StompCommand.CONNECT, authentication, null, null);
		MessageChannel channel = mock(MessageChannel.class);

		Message<?> result = stompAuthInterceptor.preSend(message, channel);

		assertSame(message, result);
		verify(jwtService, never()).extractUsername(any());
		verify(userDetailsService, never()).loadUserByUsername(any());
	}

	@Test
	void connect_shouldRejectMissingAuthorizationHeader() {
		Message<byte[]> message = stompMessage(StompCommand.CONNECT, null, null, null);
		MessageChannel channel = mock(MessageChannel.class);

		assertThrows(IllegalArgumentException.class, () -> stompAuthInterceptor.preSend(message, channel));
	}

	@Test
	void subscribe_shouldAllowChatMember() {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				"alice@mail.com",
				null,
				List.of()
		);
		Message<byte[]> message = stompMessage(StompCommand.SUBSCRIBE, authentication, null, "/topic/chat.42");
		MessageChannel channel = mock(MessageChannel.class);
		doReturn(true).when(chatService).isMemberByEmail("alice@mail.com", 42L);

		Message<?> result = stompAuthInterceptor.preSend(message, channel);

		assertSame(message, result);
		verify(chatService).isMemberByEmail(eq("alice@mail.com"), eq(42L));
	}

	@Test
	void send_shouldRejectNonMember() {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				"alice@mail.com",
				null,
				List.of()
		);
		Message<byte[]> message = stompMessage(StompCommand.SEND, authentication, null, "/app/chat.42");
		MessageChannel channel = mock(MessageChannel.class);
		doReturn(false).when(chatService).isMemberByEmail("alice@mail.com", 42L);

		assertThrows(IllegalArgumentException.class, () -> stompAuthInterceptor.preSend(message, channel));
		verify(chatService).isMemberByEmail(eq("alice@mail.com"), eq(42L));
	}

	@Test
	void subscribe_shouldRejectInvalidDestinationFormat() {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				"alice@mail.com",
				null,
				List.of()
		);
		Message<byte[]> message = stompMessage(StompCommand.SUBSCRIBE, authentication, null, "/topic/chat");
		MessageChannel channel = mock(MessageChannel.class);

		assertThrows(IllegalArgumentException.class, () -> stompAuthInterceptor.preSend(message, channel));
	}

	private Message<byte[]> stompMessage(StompCommand command, Object user, String authorizationHeader, String destination) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(command);
		if (user != null) {
			accessor.setUser((java.security.Principal) user);
		}
		if (authorizationHeader != null) {
			accessor.setNativeHeader("Authorization", authorizationHeader);
		}
		if (destination != null) {
			accessor.setDestination(destination);
		}
		accessor.setLeaveMutable(true);
		return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
	}
}
